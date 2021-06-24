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
        UserData.set(p, "session", timestamp)
    }

    fun tryAutoLogin(p : Player) : Boolean {
        val session = UserData.get(p, "session")
        val curtime = Timestamp(System.currentTimeMillis())
        val timestamp = curtime.time
        if(session != null && (parseLong(session.toString()) + 172800000 > timestamp) && p.address?.address?.hostAddress == UserData.get(p, "ip")) {
            return true
        }

        return false
    }
}