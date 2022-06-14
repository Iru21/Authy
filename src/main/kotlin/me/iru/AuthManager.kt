package me.iru

import me.iru.utils.sendWelcomeMessage
import org.bukkit.entity.Player

enum class LoginType {
    Default,
    Session,
    Premium
}

class AuthManager {
    private val authy = Authy.instance
    private val translations = Authy.translations
    private val loginProcess = Authy.loginProcess
    private val playerData = Authy.playerData
    private val effectRunner = Authy.loginProcess.EffectRunner

    fun register(p: Player, password: String) {
        playerData.create(p, password)
        loginProcess.removePlayer(p)
        p.sendMessage("${translations.getPrefix(PrefixType.REGISTER)} ${translations.get("register_success")}")

        if(authy.config.getBoolean("onJoin.teleport") && authy.config.getBoolean("onJoin.concealment")) {
            loginProcess.teleportToLocation(p)
        }
        authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} ${org.bukkit.ChatColor.YELLOW}registered with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress}")
        p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("no_pin_warning")}")
        effectRunner.runRegister(p)

        sendWelcomeMessage(p)
    }

    fun login(p: Player, type: LoginType = LoginType.Default) {
        loginProcess.removePlayer(p)
        when(type) {
            LoginType.Default -> {
                p.sendMessage("${translations.getPrefix(PrefixType.LOGIN)} ${translations.get("login_success")}")
                if(authy.config.getBoolean("onJoin.teleport") && authy.config.getBoolean("onJoin.concealment")) {
                    loginProcess.teleportToLocation(p)
                }
                authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} ${org.bukkit.ChatColor.YELLOW}logged in with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress}")
                val playerDataModel = playerData.get(p.uniqueId)!!
                if(!playerDataModel.usePin) {
                    p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("no_pin_warning")}")
                }
                effectRunner.runLogin(p)
            }
            LoginType.Session -> {
                authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} ${org.bukkit.ChatColor.YELLOW}auto logged in with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress}")
                p.sendMessage("${translations.getPrefix(PrefixType.LOGIN)} ${translations.get("autologin_success")}")
                effectRunner.runAutoLogin(p)
            }
            LoginType.Premium -> {
                effectRunner.runLogin(p)
            }
        }

        sendWelcomeMessage(p)
    }

}