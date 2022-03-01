package me.mateusz.commands

import me.mateusz.Authy
import me.mateusz.PrefixType
import me.mateusz.interfaces.ICommand
import me.mateusz.process.LoginProcess
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class cRegister(override var name: String = "register") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    val userdata = Authy.userdata
    val LoginProcess : LoginProcess = Authy.loginProcess
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            if(!LoginProcess.checkIfContains(p)) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("already_authed")}!")
                return true
            }
            if(args.size != 2) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_register_usage")}")
                return true
            }
            if(args[0] != args[1]) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_register_notidentical")}")
                return true
            }
            if(!userdata.PasswordMatchesRules(args[0])) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_register_breaksrules")}")
                return true
            }
            return if(!userdata.CheckIfExists(p)) {
                userdata.CreateOrGetUser(p, args[0])
                LoginProcess.removePlayer(p)
                p.sendMessage("${translations.getPrefix(PrefixType.REGISTER)} ${translations.get("register_success")}")
                if(authy.config.getBoolean("SendWelcomeMessage")) {
                    for(message : String in authy.config.getStringList("WelcomeMessage")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
                    }
                }
                authy.server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}Authy${ChatColor.DARK_GRAY}] ${ChatColor.YELLOW}Player ${ChatColor.WHITE}${p.name} ${ChatColor.YELLOW}registered with ip ${ChatColor.WHITE}${p.address?.address?.hostAddress}")
                if(userdata.get(p, "usePin").toString() == "false") {
                    p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("no_pin_warning")}")
                }
                LoginProcess.EffectRunner.runRegister(p)
                true
            } else {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_register_alreadyregistered")}")
                true
            }
        }
        return true
    }
}