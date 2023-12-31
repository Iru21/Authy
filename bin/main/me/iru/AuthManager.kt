package me.iru

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

    fun register(p: Player, password: String, pin: String? = null) {
        playerData.create(p, password, pin)
        loginProcess.removePlayer(p)
        p.sendMessage("${translations.getPrefix(PrefixType.REGISTER)} ${translations.get("register_success")}")
        authy.server.consoleSender.sendMessage(
            "${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] " +
                    "${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} " +
                    "${org.bukkit.ChatColor.YELLOW}registered with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress} " +
                    "${org.bukkit.ChatColor.YELLOW}and UUID ${org.bukkit.ChatColor.WHITE}${p.uniqueId}"
        )
        if(authy.config.getBoolean("sendPinSetReminder") && pin != null) p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("no_pin_warning")}")
        effectRunner.runRegister(p)
    }

    fun login(p: Player, type: LoginType = LoginType.Default) {
        loginProcess.removePlayer(p)
        when(type) {
            LoginType.Default -> {
                p.sendMessage("${translations.getPrefix(PrefixType.LOGIN)} ${translations.get("login_success")}")
                authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] " +
                        "${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} " +
                        "${org.bukkit.ChatColor.YELLOW}logged in with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress} " +
                        "${org.bukkit.ChatColor.YELLOW}and UUID ${org.bukkit.ChatColor.WHITE}${p.uniqueId}"
                )
                val authyPlayer = playerData.get(p.uniqueId)!!
                if(!authyPlayer.isPinEnabled && authy.config.getBoolean("sendPinSetReminder")) {
                    p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("no_pin_warning")}")
                }
                effectRunner.runLogin(p)
            }
            LoginType.Session -> {
                authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] " +
                        "${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} " +
                        "${org.bukkit.ChatColor.YELLOW}auto logged in with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress} " +
                        "${org.bukkit.ChatColor.YELLOW}and UUID ${org.bukkit.ChatColor.WHITE}${p.uniqueId}"
                )
                p.sendMessage("${translations.getPrefix(PrefixType.LOGIN)} ${translations.get("autologin_success")}")
                effectRunner.runAutoLogin(p)
            }
            LoginType.Premium -> {
                effectRunner.runLogin(p)
            }
        }
    }

}