package me.mateusz.commands

import me.mateusz.interfaces.ICommand
import me.mateusz.process.LoginProcess
import me.mateusz.process.Session
import me.mateusz.process.UserData
import me.mateusz.utils.HashUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.sql.Timestamp

class cRemember(override var name: String, jplugin : JavaPlugin) : ICommand {
    val plugin : JavaPlugin = jplugin
    val UserData : UserData = UserData(plugin)
    val Session : Session = Session(plugin)
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            UserData.updateIfOld(p, "session", 0)
            Session.remember(p)
            p.sendMessage("${net.md_5.bungee.api.ChatColor.of("#afffb1")}§l(✔) §7Zapamietano na 48h!")
        }
        return true
    }
}