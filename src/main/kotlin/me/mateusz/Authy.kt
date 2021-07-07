package me.mateusz

import me.mateusz.commands.*
import me.mateusz.events.LoginEvents
import me.mateusz.process.LoginProcess
import me.mateusz.process.Session
import me.mateusz.utils.CommandFilter
import me.mateusz.utils.registerCommand
import org.apache.logging.log4j.core.filter.AbstractFilter
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.logging.Filter

class Authy : JavaPlugin(), Listener {
    val version = this.description.version
    val pluginName: String = this.description.name
    val LoginProcess : LoginProcess = LoginProcess(this)
    val CommandFilter : CommandFilter = CommandFilter()

    override fun onEnable() {

        saveDefaultConfig()

        server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}$pluginName${ChatColor.DARK_GRAY}] ${ChatColor.GREEN}Enabling $version")

        server.pluginManager.registerEvents(LoginEvents(this, LoginProcess), this)

        registerCommand(this, cRegister("register", this, LoginProcess))
        registerCommand(this, cLogin("login", this, LoginProcess))
        registerCommand(this, cUnregister("unregister", this, LoginProcess))
        registerCommand(this, cRemember("remember", this))
        registerCommand(this, cAuthy("authy", this))

        with(CommandFilter) { registerFilter() }

        val Session = Session(this)

        val players = server.onlinePlayers
        for(player : Player in players) {

            lateinit var task0 : BukkitTask
            var loc = player.location
            task0 = this.server.scheduler.runTaskTimer(this, Runnable {
                if(loc.block.getRelative(BlockFace.DOWN).type.isAir) {
                    loc = Location(loc.world, loc.x, loc.y - 1, loc.z)
                } else {
                    task0.cancel()
                    player.teleport(loc)
                }
            }, 0L, 0L)

            if(Session.tryAutoLogin(player)) {
                player.sendMessage("${net.md_5.bungee.api.ChatColor.of("#afffb1")}§l(✔) §7Automatycznie zalogowano!")
                if(this.config.getBoolean("SendWelcomeMessage")) {
                    for(message : String in this.config.getStringList("WelcomeMessage")) {
                        player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message))
                    }
                }
                return
            }

            LoginProcess.addPlayer(player)
            var i = 0
            lateinit var task : BukkitTask
            task = this.server.scheduler.runTaskTimer(this, Runnable {
                if(LoginProcess.checkIfContains(player)) {
                    if(i == 240) {
                        task.cancel()
                        player.kickPlayer("§c§l(!) §7Minal czas na autoryzacje!")
                        LoginProcess.removePlayer(player)
                    }
                    LoginProcess.sendPleaseAuthMessage(player)
                    i++
                }
                else task.cancel()
            },0L, 200L)
        }

    }

    override fun onDisable() {
        server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}$pluginName${ChatColor.DARK_GRAY}] ${ChatColor.RED}Disabling $version")
    }

}