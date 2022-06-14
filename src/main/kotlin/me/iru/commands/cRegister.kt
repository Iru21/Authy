package me.iru.commands

import me.iru.Authy
import me.iru.PrefixType
import me.iru.data.Validation
import me.iru.interfaces.ICommand
import me.iru.process.LoginProcess
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class cRegister(override var name: String = "register") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    val playerData = Authy.playerData
    val loginProcess : LoginProcess = Authy.loginProcess
    val authManager = Authy.authManager

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
            val password = args[0]
            if(!Validation.passwordMatchesRules(password)) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_register_breaksrules")}")
                return true
            }
            return if(!playerData.exists(p)) {
                authManager.register(p, password)
                true
            } else {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_register_alreadyregistered")}")
                true
            }
        }
        return true
    }
}