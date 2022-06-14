package me.iru.commands

import me.iru.Authy
import me.iru.PrefixType
import me.iru.data.Validation
import me.iru.interfaces.ICommand
import me.iru.process.LoginProcess
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class cRegister(override var name: String = "register") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    val playerData = Authy.playerData
    val loginProcess : LoginProcess = Authy.loginProcess
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            if(!loginProcess.checkIfContains(p)) {
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
            if(!Validation.passwordMatchesRules(args[0])) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_register_breaksrules")}")
                return true
            }
            return if(!playerData.exists(p)) {
                playerData.create(p, args[0])
                loginProcess.removePlayer(p)
                p.sendMessage("${translations.getPrefix(PrefixType.REGISTER)} ${translations.get("register_success")}")
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
                authy.server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}Authy${ChatColor.DARK_GRAY}] ${ChatColor.YELLOW}Player ${ChatColor.WHITE}${p.name} ${ChatColor.YELLOW}registered with ip ${ChatColor.WHITE}${p.address?.address?.hostAddress}")
                p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("no_pin_warning")}")
                loginProcess.EffectRunner.runRegister(p)
                true
            } else {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_register_alreadyregistered")}")
                true
            }
        }
        return true
    }
}