package me.mateusz.process

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class LoginProcess(plugin : JavaPlugin) {
    val EffectRunner = EffectRunner(plugin)
    val inProcess = mutableListOf<UUID>()
    val UserData : UserData = UserData(plugin)

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