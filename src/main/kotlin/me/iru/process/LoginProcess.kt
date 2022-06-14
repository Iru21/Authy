package me.iru.process

import me.iru.Authy
import me.iru.PrefixType
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap

class LoginProcess {
    val authy = Authy.instance
    val translations = Authy.translations
    val EffectRunner = EffectRunner()
    val playerData = Authy.playerData

    private val inProcess = mutableListOf<UUID>()
    private val concealment = HashMap<UUID, Location>()

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
        if(playerData.exists(p)) {
            p.sendMessage(
                "${translations.getPrefix(PrefixType.WARNING)} ${
                    translations.get("loginprocess_reminder_login").format(
                        if (playerData.get(p.uniqueId)!!.usePin) translations.get("loginprocess_reminderlogin_haspin") else ""
                    )
                }"
            )
        }
        else p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("loginprocess_reminder_register")}")
    }

    fun saveLocation(p : Player) {
        concealment.set(p.uniqueId, p.location)
    }

    fun getLocation(p: Player): Location? {
        return concealment.get(p.uniqueId)
    }

    fun teleportToLocation(p: Player) {
        val loc = getLocation(p)
        if (loc != null) {
            p.teleport(loc)
        }
    }
}