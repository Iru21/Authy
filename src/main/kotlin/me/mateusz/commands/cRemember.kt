package me.mateusz.commands

import me.mateusz.interfaces.ICommand
import me.mateusz.process.Session
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class cRemember(override var name: String = "remember") : ICommand {
    val Session : Session = Session()
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            Session.remember(p)
            p.sendMessage("${net.md_5.bungee.api.ChatColor.of("#afffb1")}§l(✔) §7Zapamietano na 48h!")
        }
        return true
    }
}