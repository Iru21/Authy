package me.mateusz.process

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Long.parseLong
import java.sql.Timestamp

class Session(jplugin : JavaPlugin) {
    val plugin = jplugin
    val UserData : UserData = UserData(plugin)

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
            plugin.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} ${org.bukkit.ChatColor.YELLOW}auto logged in with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress}")
            return true
        }

        return false
    }
}