package me.mateusz

import me.mateusz.commands.*
import me.mateusz.events.LoginEvents
import me.mateusz.process.LoginProcess
import me.mateusz.process.Session
import me.mateusz.process.runJoin
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
        registerCommand(this, cPin("pin", this))

        with(CommandFilter) { registerFilter() }

        val Session = Session(this)

        val players = server.onlinePlayers
        for(player : Player in players) {
            runJoin(this, Session, LoginProcess, player)
        }

    }

    override fun onDisable() {
        server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}$pluginName${ChatColor.DARK_GRAY}] ${ChatColor.RED}Disabling $version")
    }

}