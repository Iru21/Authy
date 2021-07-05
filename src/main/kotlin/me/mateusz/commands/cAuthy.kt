package me.mateusz.commands

import me.mateusz.interfaces.ICommand
import me.mateusz.process.LoginProcess
import me.mateusz.process.UserData
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class cAuthy(override var name: String, jplugin : JavaPlugin) : ICommand {
    val plugin = jplugin
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            if(args.size != 1) {
                p.sendMessage("§8[§6Authy§8] §7Created by: §cIru21 §8- §7https://github.com/Iru21/")
                return true
            } else if(args[0].lowercase() == "reload") {
                plugin.saveConfig()
                p.sendMessage("§8[§6Authy§8] §7Reloaded §cconfig§7!")
            }
        }
        return true
    }
}