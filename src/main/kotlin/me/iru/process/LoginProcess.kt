package me.iru.process

import me.iru.Authy
import me.iru.PrefixType
import me.iru.utils.sendVersionDownload
import me.iru.utils.sendWelcomeMessage
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import java.util.*

class LoginProcess {
    val authy = Authy.instance
    val translations = Authy.translations
    val EffectRunner = EffectRunner()
    val playerData = Authy.playerData

    private val inProcess = mutableSetOf<UUID>()

    fun addPlayer(p : Player) {
        inProcess.add(p.uniqueId)
    }

    fun removePlayer(p : Player) {
        p.fallDistance = 0F
        p.removePotionEffect(PotionEffectType.BLINDNESS)
        PreLoginDataStore.restore(p)

        sendWelcomeMessage(p)
        sendVersionDownload(p)

        inProcess.remove(p.uniqueId)
    }

    fun contains(e : Player) : Boolean {
        return inProcess.contains(e.uniqueId)
    }

    fun sendPleaseAuthMessage( p : Player) {
        if(playerData.exists(p.uniqueId)) {
            p.sendMessage(
                "${translations.getPrefix(PrefixType.WARNING)} ${
                    translations.get("loginprocess_reminder_login").format(
                        if (playerData.get(p.uniqueId)!!.isPinEnabled) translations.get("loginprocess_reminder_pin") else ""
                    )
                }"
            )
        }
        else p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("loginprocess_reminder_register").format(
            if (authy.config.getBoolean("requirePin")) translations.get("loginprocess_reminder_pin") else ""
        )}")
    }
}