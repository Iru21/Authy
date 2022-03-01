package me.mateusz.commands

import me.mateusz.Authy
import me.mateusz.PrefixType
import me.mateusz.interfaces.ICommand
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class cLogin(override var name: String = "login") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    val userdata = Authy.userdata
    val LoginProcess = Authy.loginProcess
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            val shouldUsePin = userdata.get(p, "usePin") == "true"
            if(!LoginProcess.checkIfContains(p)) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("already_authed")}")
                return true
            }
            if(shouldUsePin) {
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
            if(!userdata.CheckIfExists(p)) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_notregistered")}")
                return true
            }
            return if(!userdata.Validate(p, args[0], "pass")) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_wrongpassword")}")
                true
            } else {
                if(shouldUsePin) {
                    if(!userdata.Validate(p, args[1], "pin")) {
                        p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_wrongpin")}")
                        return true
                    }
                }
                LoginProcess.removePlayer(p)
                p.sendMessage("${translations.getPrefix(PrefixType.LOGIN)} ${translations.get("login_success")}")
                if(authy.config.getBoolean("SendWelcomeMessage")) {
                    for(message : String in authy.config.getStringList("WelcomeMessage")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
                    }
                }
                authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} ${org.bukkit.ChatColor.YELLOW}logged in with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress}")
                if(userdata.get(p, "usePin").toString() == "false") {
                    p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("no_pin_warning")}")
                }
                LoginProcess.EffectRunner.runLogin(p)
                true
            }

        }
        return true
    }
}