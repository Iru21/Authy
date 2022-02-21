package me.mateusz.commands

import me.mateusz.Authy
import me.mateusz.PrefixType
import me.mateusz.interfaces.ICommand
import me.mateusz.process.UserData
import me.mateusz.utils.HashUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class cPin(override var name: String = "pin") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    val UserData : UserData = UserData()

    fun getStatus(p: Player): Boolean {
        return UserData.get(p, "usePin").toString() == "true"
    }

    fun getStatusTranslated(p: Player): String {
        return if (getStatus(p)) translations.get("enabled") else translations.get("disabled")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            UserData.updateIfOld(p, "usePin", false)
            UserData.updateIfOld(p, "pin", "not_set")
            if(args.isEmpty()) {
                val pinColor = translations.getColor("prefix_pin_color")
                val status = if(getStatus(p)) translations.get("enabled") else translations.get("disabled")
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
                if(UserData.get(p, "pin") == "not_set") {
                    p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_pin_setpinbeforetoggle")}")
                    return true
                }
                UserData.set(p, "usePin", !getStatus(p))
                p.sendMessage("${translations.getPrefix(PrefixType.PIN)} ${translations.get("command_pin_toggled").format(getStatusTranslated(p))}")
            } else if(args[0].lowercase() == "set") {
                if(args.size != 2) {
                    p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_pin_setusage")}")
                    return true
                }
                if(!UserData.PinMatchesRules(args[1])) {
                    p.sendMessage("${translations.getPrefix(PrefixType.ERROR)} ${translations.get("command_pin_breaksrules")}")
                    return true
                }
                UserData.set(p, "pin", HashUtil.toSHA256(args[1]))
                p.sendMessage("${translations.getPrefix(PrefixType.PIN)} ${translations.get("command_pin_success")}")
            }
        }
        return true
    }
}