package me.mateusz.process

import me.mateusz.Authy
import org.bukkit.entity.Player
import java.util.*

class LoginProcess() {
    val authy = Authy.instance
    val EffectRunner = EffectRunner()
    val inProcess = mutableListOf<UUID>()
    val UserData : UserData = UserData()

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
        if(UserData.CheckIfExists(p)) {

            p.sendMessage("§6§l(!) §7Zaloguj sie uzywajac §8/§flogin §8[§fhaslo§8]${if(UserData.get(p, "usePin") == "true") " [§fpin§8]" else ""}")
        }
        else p.sendMessage("§6§l(!) §7Zarejestruj sie uzywajac §8/§fregister §8[§fhaslo§8] [§fpowtorz haslo§8]")
    }
}