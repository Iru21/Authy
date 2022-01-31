package me.mateusz.commands

import me.mateusz.Authy
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
    val HashUtil : HashUtil = HashUtil()

    fun getStatus(p: Player): Boolean {
        return UserData.get(p, "usePin") == "true"
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            UserData.updateIfOld(p, "usePin", false)
            UserData.updateIfOld(p, "pin", "not_set")
            if(args.isEmpty()) {
                val status = if(getStatus(p)) translations.get("enabled") else translations.get("disabled")
                p.sendMessage(translations.get("pincommand_info_bar"))
                p.sendMessage("")
                p.sendMessage(translations.get("pincommand_info_status").format(status))
                p.sendMessage("")
                p.sendMessage(translations.get("pincommand_info_split"))
                p.sendMessage("")
                p.sendMessage(translations.get("pincommand_info_togglehelp"))
                p.sendMessage(translations.get("pincommand_info_sethelp"))
                p.sendMessage("")
                p.sendMessage(translations.get("pincommand_info_bar"))
            } else if(args[0].lowercase() == "toggle") {
                if(UserData.get(p, "pin") == "not_set") {
                    p.sendMessage("§c§l(!) ${translations.get("command_pin_setpinbeforetoggle")}")
                    return true
                }
                val current = if (getStatus(p)) translations.get("enabled") else translations.get("disabled")
                UserData.set(p, "usePin", !getStatus(p))
                p.sendMessage("§a§l(✔) ${translations.get("command_pin_toggled").format(current)}")
            } else if(args[0].lowercase() == "set") {
                if(args.size != 2) {
                    p.sendMessage("§c§l(!) ${translations.get("command_pin_setusage")}")
                    return true
                }
                if(!UserData.PinMatchesRules(args[1])) {
                    p.sendMessage("§c§l(!) ${translations.get("command_pin_breaksrules")}")
                    return true
                }
                UserData.set(p, "pin", HashUtil.toSHA256(args[1]))
                p.sendMessage("§a§l(✔) ${translations.get("command_pin_success")}")
            }
        }
        return true
    }
}