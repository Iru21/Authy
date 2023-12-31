package me.iru.commands

import me.iru.Authy
import me.iru.PrefixType
import me.iru.interfaces.ICommand
import me.iru.utils.HashUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import me.iru.validation.PinValidation
import me.iru.validation.getPinRule


class cPin(override var name: String = "pin") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    val playerData = Authy.playerData

    private fun getStatusTranslated(p: Player): String {
        val authyPlayer = playerData.get(p.uniqueId)!!
        return if (authyPlayer.isPinEnabled) translations.get("enabled") else translations.get("disabled")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            val authyPlayer = playerData.get(p.uniqueId)!!
            if(args.isEmpty()) {
                val pinColor = translations.getColor("prefix_pin_color")
                val status = getStatusTranslated(p)
                p.sendMessage(translations.get("pincommand_info_bar").format(pinColor, pinColor))
                p.sendMessage("")
                p.sendMessage(translations.get("pincommand_info_status").format(status))
                p.sendMessage("")
                p.sendMessage(translations.get("pincommand_info_split"))
                p.sendMessage("")
                p.sendMessage(translations.get("pincommand_info_togglehelp"))
                p.sendMessage(translations.get("pincommand_info_sethelp"))
                p.sendMessage("")
                p.sendMessage(translations.get("pincommand_info_bar").format(pinColor, pinColor))
            } else if(args[0].lowercase() == "toggle") {
                if(authyPlayer.pin == null) {
                    p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_pin_setpinbeforetoggle")}")
                    return true
                }
                if(authyPlayer.isPinEnabled && authy.config.getBoolean("requirePin")) {
                    p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_pin_required")}")
                    return true
                }
                authyPlayer.isPinEnabled = !authyPlayer.isPinEnabled
                playerData.update(authyPlayer)
                p.sendMessage("${translations.getPrefix(PrefixType.PIN)} ${translations.get("command_pin_toggled").format(getStatusTranslated(p))}")
            } else if(args[0].lowercase() == "set") {
                if(args.size != 2) {
                    p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_pin_setusage")}")
                    return true
                }
                if(!PinValidation.matchesRules(args[1])) {
                    val rule = getPinRule()
                    p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_pin_breaksrules").format(rule.minLength, rule.maxLength)}")
                    return true
                }
                authyPlayer.pin = HashUtil.toSHA256(args[1])
                playerData.update(authyPlayer)
                p.sendMessage("${translations.getPrefix(PrefixType.PIN)} ${translations.get("command_pin_success")}")
            }
        }
        return true
    }
}