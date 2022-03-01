package me.mateusz.process

import me.mateusz.Authy
import me.mateusz.PrefixType
import net.md_5.bungee.api.ChatColor
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

fun runJoin(player: Player) {

    val authy = Authy.instance
    val translations = Authy.translations
    val LoginProcess = Authy.loginProcess
    val Session = Authy.session
    val userdata = Authy.userdata

    userdata.updateIfOld(player, "session", 0)
    userdata.updateIfOld(player, "usePin", false)
    userdata.updateIfOld(player, "pin", "not_set")

    if(!player.hasPlayedBefore() && authy.config.getBoolean("onFirstJoin.teleport")) {
        val x = authy.config.getDouble("onFirstJoin.x")
        val y = authy.config.getDouble("onFirstJoin.y") + 0.1
        val z = authy.config.getDouble("onFirstJoin.z")
        player.teleport(Location(player.world, x, y, z))
    } else if(authy.config.getBoolean("onJoin.teleport")) {
        val x = authy.config.getDouble("onJoin.x")
        val y = authy.config.getDouble("onJoin.y") + 0.1
        val z = authy.config.getDouble("onJoin.z")
        player.teleport(Location(player.world, x, y, z))
    }
    var setFly = false
    if(player.isFlying) {
        setFly = true
        player.isFlying = false
    }

    var task0 : BukkitTask? = null
    var loc = player.location
    task0 = authy.server.scheduler.runTaskTimer(authy, Runnable {
        if(loc.block.getRelative(BlockFace.DOWN).type.isAir) {
            loc = Location(loc.world, loc.x, loc.y - 1, loc.z)
        } else {
            task0!!.cancel()
            player.teleport(loc)
        }
    }, 0L, 0L)

    if(setFly) player.isFlying = true

    if(Session.tryAutoLogin(player)) {
        player.sendMessage("${translations.getPrefix(PrefixType.LOGIN)} ${translations.get("autologin_success")}")
        if(authy.config.getBoolean("SendWelcomeMessage")) {
            for(message : String in authy.config.getStringList("WelcomeMessage")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
            }
        }
        return
    }

    LoginProcess.addPlayer(player)
    var i = 0
    var task : BukkitTask? = null
    task = authy.server.scheduler.runTaskTimer(authy, Runnable {
        if(LoginProcess.checkIfContains(player)) {
            if(i == 240) {
                task!!.cancel()
                player.kickPlayer("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("timedout_error")}")
                LoginProcess.removePlayer(player)
            }
            LoginProcess.sendPleaseAuthMessage(player)
            i++
        }
        else task!!.cancel()
    },0L, 200L)
}