package me.iru.data

import me.iru.Authy
import me.iru.LoginType
import org.bukkit.entity.Player
import java.sql.Timestamp

class Session {
    val authy = Authy.instance
    val playerData = Authy.playerData
    val authManager = Authy.authManager

    fun remember(p : Player) {
        val authyPlayer = playerData.get(p.uniqueId)!!
        authyPlayer.session = Timestamp(System.currentTimeMillis()).time
        authyPlayer.ip = p.address?.address?.hostAddress!!
        playerData.update(authyPlayer)
    }

    fun tryAutoLogin(p : Player) : Boolean {
        val authyPlayer = playerData.get(p.uniqueId) ?: return false
        val now = Timestamp(System.currentTimeMillis()).time
        val hours = authy.config.getInt("sessionExpiresIn")
        val passes = if(hours > 500 || hours < 0) true
                     else authyPlayer.session + (hours * 3600000L) > now
        if(passes && p.address?.address?.hostAddress == authyPlayer.ip) {
            authManager.login(p, LoginType.Session)
            return true
        }

        return false
    }

}