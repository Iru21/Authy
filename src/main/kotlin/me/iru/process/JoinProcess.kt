package me.iru.process

import me.iru.Authy
import me.iru.PrefixType
import me.iru.data.migration.Migration
import me.iru.utils.hasValidName
import me.iru.utils.teleportToGround
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask

class JoinProcess(private val player: Player) {

    private val authy = Authy.instance
    private val loginProcess = Authy.loginProcess
    private val translations = Authy.translations
    private val session = Authy.session

    fun run() {
        if(!hasValidName(player.name) && authy.config.getBoolean("nameValidation")) player.kickPlayer(translations.get("invalid_username"))

        Migration.updatePlayer(player)

        PreLoginDataStore.save(player)

        if(session.tryAutoLogin(player)) return

        joinTeleports()
        teleportToGround(player)

        // Place premium check here

        loginProcess.addPlayer(player)

        if(authy.config.getBoolean("effects.blindness")) {
            player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, Int.MAX_VALUE, 255))
        }

        check()
    }

    private fun joinTeleports() {
        if(!this.player.hasPlayedBefore() && authy.config.getBoolean("onFirstJoin.teleport")) {
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
    }

    private fun check() {
        var i = 0
        var task : BukkitTask? = null
        task = authy.server.scheduler.runTaskTimer(authy, Runnable {
            if(loginProcess.contains(player)) {
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