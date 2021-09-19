package me.mateusz.commands

import me.mateusz.interfaces.ICommand
import me.mateusz.process.LoginProcess
import me.mateusz.process.UserData
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class cRegister(override var name: String, jplugin : JavaPlugin, preLoginProcess : LoginProcess) : ICommand {
    val UserData : UserData = UserData(jplugin)
    val LoginProcess : LoginProcess = preLoginProcess
    val plugin = jplugin
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            if(!LoginProcess.checkIfContains(p)) {
                p.sendMessage("§c§l(!) §7Jestes juz uwierzytelniony!")
                return true
            }
            if(args.size != 2) {
                p.sendMessage("§c§l(!) §7Uzycie: §8/§fregister §8[§fhaslo§8] [§fpowtorz haslo§8]")
                return true
            }
            if(args[0] != args[1]) {
                p.sendMessage("§c§l(!) §7Hasla sie nie zgadzaja")
                return true
            }
            if(!UserData.PasswordMatchesRules(args[0])) {
                p.sendMessage("§c§l(!) §7Haslo musi miec przynajmniej jedna duza litere, liczbe i zawierac 6 lub wiecej znakow!")
                return true
            }
            return if(!UserData.CheckIfExists(p)) {
                UserData.CreateOrGetUser(p, args[0])
                LoginProcess.removePlayer(p)
                p.sendMessage("${net.md_5.bungee.api.ChatColor.of("#CDFF00")}§l(✔) §7Zarejestrowano!")
                if(plugin.config.getBoolean("SendWelcomeMessage")) {
                    for(message : String in plugin.config.getStringList("WelcomeMessage")) {
                        p.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message))
                    }
                }
                plugin.server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}Authy${ChatColor.DARK_GRAY}] ${ChatColor.YELLOW}Player ${ChatColor.WHITE}${p.name} ${ChatColor.YELLOW}registered with ip ${ChatColor.WHITE}${p.address?.address?.hostAddress}")
                if(UserData.get(p, "usePin") == "false") {
                    p.sendMessage("§6§l(!) §cNie masz wlaczonego pinu§8! §7Dla bezpieczenstwa ustaw go pod §8/§fpin")
                }
                LoginProcess.EffectRunner.runRegister(p)
                true
            } else {
                p.sendMessage("§c§l(!) §7Jestes juz zarejestrowany!")
                true
            }
        }
        return true
    }
}