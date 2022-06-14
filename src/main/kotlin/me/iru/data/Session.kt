package me.iru.data

import me.iru.Authy
import me.iru.LoginType
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
        val playerDataModel = playerData.get(p.uniqueId)!!
        playerDataModel.session = timestamp
        playerDataModel.ip = p.address?.address?.hostAddress!!
        playerData.save(playerDataModel)
    }

    fun tryAutoLogin(p : Player) : Boolean {
        val playerDataModel = playerData.get(p.uniqueId)
        if(playerDataModel == null) return false
        val session = playerDataModel.session
        val curtime = Timestamp(System.currentTimeMillis())
        val timestamp = curtime.time
        if((parseLong(session.toString()) + 172800000 > timestamp) && p.address?.address?.hostAddress == playerDataModel.ip) {
            authManager.login(p, LoginType.Session)
            return true
        }

        return false
    }
}