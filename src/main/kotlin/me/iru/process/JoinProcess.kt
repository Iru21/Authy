package me.iru.process

import me.iru.Authy
import me.iru.PrefixType
import me.iru.data.Migration
import net.md_5.bungee.api.ChatColor
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class JoinProcess(private val player: Player) {

    private val authy = Authy.instance
    private val loginProcess = Authy.loginProcess
    private val translations = Authy.translations
    private val session = Authy.session

    fun run() {
        Migration.updatePlayer(player)

        joinTeleports()
        teleportToGround()
        if(tryAutoLogin()) return

        loginProcess.addPlayer(player)

        check()
    }

    private fun joinTeleports() {
        if(!this.player.hasPlayedBefore() && authy.config.getBoolean("onFirstJoin.teleport")) {
            val x = authy.config.getDouble("onFirstJoin.x")
            val y = authy.config.getDouble("onFirstJoin.y") + 0.1
            val z = authy.config.getDouble("onFirstJoin.z")
            player.teleport(Location(player.world, x, y, z))
        } else if(authy.config.getBoolean("onJoin.teleport")) {
            if(authy.config.getBoolean("onJoin.concealment")) {
                loginProcess.saveLocation(player)
            }
            val x = authy.config.getDouble("onJoin.x")
            val y = authy.config.getDouble("onJoin.y") + 0.1
            val z = authy.config.getDouble("onJoin.z")
            player.teleport(Location(player.world, x, y, z))
        }
    }

    private fun teleportToGround() {
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
    }

    private fun tryAutoLogin(): Boolean {
        return if(session.tryAutoLogin(player)) {
            player.sendMessage("${translations.getPrefix(PrefixType.LOGIN)} ${translations.get("autologin_success")}")
            if(authy.config.getBoolean("SendWelcomeMessage")) {
                for(message : String in authy.config.getStringList("WelcomeMessage")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
                }
            }
            true
        }
        else false
    }

    private fun check() {
        var i = 0
        var task : BukkitTask? = null
        task = authy.server.scheduler.runTaskTimer(authy, Runnable {
            if(loginProcess.checkIfContains(player)) {
                if(i == 10) {
                    task!!.cancel()
                    player.kickPlayer("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("timedout_error")}")
                    loginProcess.removePlayer(player)
                } else {
                    loginProcess.sendPleaseAuthMessage(player)
                    i++
                }
            }
            else task!!.cancel()
        },0L, 200L)
    }

}