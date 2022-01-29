package me.mateusz.commands

import me.mateusz.Authy
import me.mateusz.interfaces.ICommand
import me.mateusz.process.LoginProcess
import me.mateusz.process.UserData
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.lang.Exception

class cUnregister(override var name: String = "unregister") : ICommand {
    val authy = Authy.instance
    val UserData : UserData = UserData()
    val LoginProcess : LoginProcess = Authy.loginProcess
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            UserData.DeleteUser(p)
            p.sendMessage("§6§l(!) §7Odrejestrowano!")
            LoginProcess.EffectRunner.runUnregister(p)
            authy.server.scheduler.runTaskLater(authy, Runnable {
                p.kickPlayer("§6§l(!) §7Wejdz ponownie i zarejestruj sie!")
            }, 40L)
            return true

        }
        else if(sender is ConsoleCommandSender) {
            if(args.isEmpty()) {
                sender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${authy.description.name}${ChatColor.DARK_GRAY}] ${ChatColor.RED}Uzycie: unregister [gracz]")
                return true
            }
            try {
                @Suppress("DEPRECATION")
                val p = Bukkit.getOfflinePlayer(args[0])
                if(p.hasPlayedBefore()) {
                    val b = UserData.DeleteUser(p)
                    if(b) sender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${authy.description.name}${ChatColor.DARK_GRAY}] ${ChatColor.GREEN}Odrejestrowano!")
                    else sender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${authy.description.name}${ChatColor.DARK_GRAY}] ${ChatColor.RED}Uzytkownik nie jest zarejestrowany!")
                }
                else sender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${authy.description.name}${ChatColor.DARK_GRAY}] ${ChatColor.RED}Nie znalazlem gracza!")
            } catch (e : Exception) {
                sender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}${authy.description.name}${ChatColor.DARK_GRAY}] ${ChatColor.DARK_RED}Wystapil blad!")
            }
            return true
        }
        return true
    }
}