package me.iru.data

import me.iru.Authy
import org.bukkit.entity.Player
import java.lang.Long.parseLong
import java.sql.Timestamp

class Session {
    val authy = Authy.instance
    val playerData = Authy.playerData
    val effectRunner = Authy.loginProcess.EffectRunner

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
            authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} ${org.bukkit.ChatColor.YELLOW}auto logged in with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress}")
            effectRunner.runAutoLogin(p)
            return true
        }

        return false
    }
}