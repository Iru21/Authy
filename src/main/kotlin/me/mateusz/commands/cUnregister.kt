package me.mateusz.commands

import me.mateusz.Authy
import me.mateusz.PrefixType
import me.mateusz.interfaces.ICommand
import me.mateusz.process.LoginProcess
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.lang.Exception

class cUnregister(override var name: String = "unregister") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    val playerData = Authy.playerData
    val LoginProcess : LoginProcess = Authy.loginProcess
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            playerData.delete(p.uniqueId)
            p.sendMessage("${translations.getPrefix(PrefixType.UNREGISTER)} ${translations.get("unregister_success")}")
            LoginProcess.EffectRunner.runUnregister(p)
            authy.server.scheduler.runTaskLater(authy, Runnable {
                p.kickPlayer("${translations.getPrefix(PrefixType.UNREGISTER)} ${translations.get("command_unregister_successkick")}")
            }, 40L)
            return true

        }
        else if(sender is ConsoleCommandSender) {
            if(args.size != 1) {
                sender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${authy.description.name}${ChatColor.DARK_GRAY}] ${ChatColor.RED}Usage: unregister [player name]")
                return true
            }
            try {
                @Suppress("DEPRECATION")
                val p = Bukkit.getOfflinePlayer(args[0])
                if(playerData.delete(p.uniqueId)) sender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${authy.description.name}${ChatColor.DARK_GRAY}] ${ChatColor.GREEN}Unregistered!")
                else sender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${authy.description.name}${ChatColor.DARK_GRAY}] ${ChatColor.RED}That player is not registered!")
            } catch (e : Exception) {
                sender.sendMessage(e.message)
                sender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${authy.description.name}${ChatColor.DARK_GRAY}] ${ChatColor.DARK_RED}There has been an error!")
            }
            return true
        }
        return true
    }
}