package me.iru.process

import me.iru.Authy
import me.iru.PrefixType
import me.iru.data.migration.Migration
import me.iru.utils.TeleportUtil
import me.iru.utils.hasValidName
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
        TeleportUtil.teleportToValidPlace(player)

        // Place premium check here

        loginProcess.addPlayer(player)

        if(authy.config.getBoolean("effects.blindness")) {
            player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, Int.MAX_VALUE, 255))
        }

        check()
    }

    private fun joinTeleports() {
        var key = "onJoin"
        if(!this.player.hasPlayedBefore()) {
            key = "onFirstJoin"
        }
        if(authy.config.getBoolean("onFirstJoin.teleport") || authy.config.getBoolean("onJoin.teleport")) {
            val x = authy.config.getDouble("$key.x")
            val y = authy.config.getDouble("$key.y") + 0.1
            val z = authy.config.getDouble("$key.z")
            val yaw = authy.config.getDouble("$key.yaw").toFloat()
            val pitch = authy.config.getDouble("$key.pitch").toFloat()
            val world = authy.config.getString("$key.world") ?: "world"
            player.teleport(Location(authy.server.getWorld(world), x, y, z, yaw, pitch))
        }
    }

    private fun check() {
        lateinit var messageTask : BukkitTask
        messageTask = authy.server.scheduler.runTaskTimer(authy, Runnable {
            if(loginProcess.contains(player)) {
                loginProcess.sendPleaseAuthMessage(player)
            }
            else messageTask.cancel()
        },0L, 200L)

        authy.server.scheduler.runTaskLater(authy, Runnable {
            if(loginProcess.contains(player)) {
                messageTask.cancel()
                player.kickPlayer("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("timedout_error")}")
                loginProcess.removePlayer(player)
            }
        }, authy.config.getLong("timeout") * 20L)
    }



}