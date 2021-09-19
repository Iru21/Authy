package me.mateusz.process

import net.md_5.bungee.api.ChatColor
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

fun runJoin(plugin: JavaPlugin, Session: Session, LoginProcess: LoginProcess, player: Player) {

    val UserData = UserData(plugin)

    UserData.updateIfOld(player, "session", 0)
    UserData.updateIfOld(player, "usePin", false)
    UserData.updateIfOld(player, "pin", "not_set")

    if(!player.hasPlayedBefore() && plugin.config.getBoolean("onFirstJoin.teleport")) {
        val x = plugin.config.getDouble("onFirstJoin.x")
        val y = plugin.config.getDouble("onFirstJoin.y") + 0.1
        val z = plugin.config.getDouble("onFirstJoin.z")
        player.teleport(Location(player.world, x, y, z))
    } else if(plugin.config.getBoolean("onJoin.teleport")) {
        val x = plugin.config.getDouble("onJoin.x")
        val y = plugin.config.getDouble("onJoin.y") + 0.1
        val z = plugin.config.getDouble("onJoin.z")
        player.teleport(Location(player.world, x, y, z))
    }
    var setFly = false
    if(player.isFlying) {
        setFly = true
        player.isFlying = false
    }

    lateinit var task0 : BukkitTask
    var loc = player.location
    task0 = plugin.server.scheduler.runTaskTimer(plugin, Runnable {
        if(loc.block.getRelative(BlockFace.DOWN).type.isAir) {
            loc = Location(loc.world, loc.x, loc.y - 1, loc.z)
        } else {
            task0.cancel()
            player.teleport(loc)
        }
    }, 0L, 0L)

    if(setFly) player.isFlying = true

    if(Session.tryAutoLogin(player)) {
        player.sendMessage("${ChatColor.of("#afffb1")}§l(✔) §7Automatycznie zalogowano!")
        if(plugin.config.getBoolean("SendWelcomeMessage")) {
            for(message : String in plugin.config.getStringList("WelcomeMessage")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
            }
        }
        return
    }

    LoginProcess.addPlayer(player)
    var i = 0
    lateinit var task : BukkitTask
    task = plugin.server.scheduler.runTaskTimer(plugin, Runnable {
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