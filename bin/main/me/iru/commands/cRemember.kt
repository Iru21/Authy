package me.iru.commands

import me.iru.Authy
import me.iru.PrefixType
import me.iru.interfaces.ICommand
import me.iru.data.Session
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class cRemember(override var name: String = "remember") : ICommand {
    val Session : Session = Session()
    val authy = Authy.instance
    val translations = Authy.translations
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            Session.remember(p)
            val hours = authy.config.getInt("sessionExpiresIn")
            p.sendMessage("${translations.getPrefix(PrefixType.REMEMBER)} ${translations.get("command_remember_success").format(
                if(hours > 500 || hours < 0) "∞" else hours
            )}")
        }
        return true
    }
}