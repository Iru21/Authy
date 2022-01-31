package me.mateusz.commands

import me.mateusz.Authy
import me.mateusz.PrefixType
import me.mateusz.interfaces.ICommand
import me.mateusz.process.Session
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class cRemember(override var name: String = "remember") : ICommand {
    val Session : Session = Session()
    val translations = Authy.translations
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            Session.remember(p)
            p.sendMessage("${translations.getPrefix(PrefixType.REMEMBER)} ${translations.get("command_remember_success")}")
        }
        return true
    }
}