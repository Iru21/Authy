package me.iru.utils

import me.iru.Authy
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player

fun sendWelcomeMessage(p: Player) {

    val authy = Authy.instance

    if(authy.config.getBoolean("welcomeMessage.enabled")) {
        for(message : String in authy.config.getStringList("welcomeMessage.text")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
        }
    }
}