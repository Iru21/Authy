package me.iru.commands

import me.iru.Authy
import me.iru.PrefixType
import me.iru.interfaces.ICommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import me.iru.validation.PasswordValidation
import me.iru.validation.PinValidation

class cLogin(override var name: String = "login") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    val playerData = Authy.playerData
    val loginProcess = Authy.loginProcess
    val authManager = Authy.authManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            val authyPlayer = playerData.get(p.uniqueId)
            if(authyPlayer == null || !playerData.exists(p.uniqueId)) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_notregistered")}")
                return true
            }
            if(!loginProcess.contains(p)) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("already_authed")}")
                return true
            }
            if(authyPlayer.isPinEnabled) {
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
            return if(!PasswordValidation.check(p.uniqueId, args[0])) {
                p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_wrongpassword")}")
                true
            } else {
                if(authyPlayer.isPinEnabled) {
                    if(!PinValidation.check(p.uniqueId, args[1])) {
                        p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_login_wrongpin")}")
                        return true
                    }
                }
                authManager.login(p)
                true
            }

        }
        return true
    }
}