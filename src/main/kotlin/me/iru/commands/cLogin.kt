package me.iru.commands

import me.iru.Authy
import me.iru.PrefixType
import me.iru.data.Validation
import me.iru.interfaces.ICommand
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class cLogin(override var name: String = "login") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    val playerData = Authy.playerData
    val loginProcess = Authy.loginProcess
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            val playerDataModel = playerData.get(p.uniqueId)
            if(playerDataModel == null || !playerData.exists(p)) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_notregistered")}")
                return true
            }
            if(!loginProcess.checkIfContains(p)) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("already_authed")}")
                return true
            }
            if(playerDataModel.usePin) {
                if(args.size != 2) {
                    p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_usagepin")}")
                    return true
                }
            } else {
                if(args.size != 1) {
                    p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_usage")}")
                    return true
                }
            }
            return if(!Validation.checkPassword(p.uniqueId, args[0])) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_wrongpassword")}")
                true
            } else {
                if(playerDataModel.usePin) {
                    if(!Validation.checkPin(p.uniqueId, args[1])) {
                        p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_wrongpin")}")
                        return true
                    }
                }
                loginProcess.removePlayer(p)
                p.sendMessage("${translations.getPrefix(PrefixType.LOGIN)} ${translations.get("login_success")}")
                if(authy.config.getBoolean("SendWelcomeMessage")) {
                    for(message : String in authy.config.getStringList("WelcomeMessage")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
                    }
                }
                if(authy.config.getBoolean("onJoin.teleport") && authy.config.getBoolean("onJoin.concealment")) {
                    val loc = loginProcess.getLocation(p)
                    if (loc != null) {
                        p.teleport(loc)
                    }
                }
                authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} ${org.bukkit.ChatColor.YELLOW}logged in with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress}")
                if(!playerDataModel.usePin) {
                    p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("no_pin_warning")}")
                }
                loginProcess.EffectRunner.runLogin(p)
                true
            }

        }
        return true
    }
}