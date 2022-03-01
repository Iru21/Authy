package me.mateusz.process

import me.mateusz.Authy
import me.mateusz.PrefixType
import org.bukkit.entity.Player
import java.util.*

class LoginProcess {
    val authy = Authy.instance
    val translations = Authy.translations
    val EffectRunner = EffectRunner()
    val inProcess = mutableListOf<UUID>()
    val userdata = Authy.userdata

    fun addPlayer(p : Player) {
        inProcess.add(p.uniqueId)
    }

    fun removePlayer(p : Player) {
        p.fallDistance = 0F
        inProcess.remove(p.uniqueId)
    }

    fun checkIfContains(e : Player) : Boolean {
        return inProcess.contains(e.uniqueId)
    }

    fun sendPleaseAuthMessage( p : Player) {
        if(userdata.CheckIfExists(p)) {
            p.sendMessage(
                "${translations.getPrefix(PrefixType.WARNING)} ${
                    translations.get("loginprocess_reminder_login").format(
                        if (userdata.get(
                                p,
                                "usePin"
                            ) == "true"
                        ) translations.get("loginprocess_reminderlogin_haspin") else ""
                    )
                }"
            )
        }
        else p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("loginprocess_reminder_register")}")
    }
}