package me.iru.data

import me.iru.Authy
import me.iru.LoginType
import me.iru.utils.TeleportUtil
import org.bukkit.entity.Player
import java.lang.Long.parseLong
import java.sql.Timestamp

class Session {
    val authy = Authy.instance
    val playerData = Authy.playerData
    val authManager = Authy.authManager

    fun remember(p : Player) {
        val curtime = Timestamp(System.currentTimeMillis())
        val timestamp = curtime.time
        val authyPlayer = playerData.get(p.uniqueId)!!
        authyPlayer.session = timestamp
        authyPlayer.ip = p.address?.address?.hostAddress!!
        playerData.update(authyPlayer)
    }

    fun tryAutoLogin(p : Player) : Boolean {
        val authyPlayer = playerData.get(p.uniqueId) ?: return false
        val session = authyPlayer.session
        val curtime = Timestamp(System.currentTimeMillis())
        val timestamp = curtime.time
        if((parseLong(session.toString()) + (authy.config.getInt("sessionExpiresIn") * 3600000) > timestamp) && p.address?.address?.hostAddress == authyPlayer.ip) {
            TeleportUtil.teleportToValidPlace(p)
            authManager.login(p, LoginType.Session)
            return true
        }

        return false
    }

}