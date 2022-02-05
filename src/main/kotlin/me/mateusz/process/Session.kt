package me.mateusz.process

import me.mateusz.Authy
import org.bukkit.entity.Player
import java.lang.Long.parseLong
import java.sql.Timestamp

class Session {
    val authy = Authy.instance
    val UserData : UserData = UserData()
    val EffectRunner = EffectRunner()

    fun remember(p : Player) {
        val curtime = Timestamp(System.currentTimeMillis())
        val timestamp = curtime.time
        val ip = p.address?.address?.hostAddress
        UserData.set(p, "session", timestamp)
        if (ip != null) {
            UserData.set(p,"ip", ip)
        }
    }

    fun tryAutoLogin(p : Player) : Boolean {
        val session = UserData.get(p, "session")
        val curtime = Timestamp(System.currentTimeMillis())
        val timestamp = curtime.time
        if(session != null && (parseLong(session.toString()) + 172800000 > timestamp) && p.address?.address?.hostAddress == UserData.get(p, "ip")) {
            authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} ${org.bukkit.ChatColor.YELLOW}auto logged in with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress}")
            EffectRunner.runAutoLogin(p)
            return true
        }

        return false
    }
}