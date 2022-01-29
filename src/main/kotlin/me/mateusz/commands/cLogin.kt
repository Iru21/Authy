package me.mateusz.commands

import me.mateusz.Authy
import me.mateusz.interfaces.ICommand
import me.mateusz.process.UserData
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class cLogin(override var name: String = "login") : ICommand {
    val authy = Authy.instance
    val UserData : UserData = UserData()
    val LoginProcess = Authy.loginProcess
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            val shouldUsePin = UserData.get(p, "usePin") == "true"
            if(!LoginProcess.checkIfContains(p)) {
                p.sendMessage("§c§l(!) §7Jestes juz uwierzytelniony!")
                return true
            }
            if(shouldUsePin) {
                if(args.size != 2) {
                    p.sendMessage("§c§l(!) §7Uzycie: §8/§flogin §8[§fhaslo§8] [§fpin§8]")
                    return true
                }
            } else {
                if(args.size != 1) {
                    p.sendMessage("§c§l(!) §7Uzycie: §8/§flogin §8[§fhaslo§8]")
                    return true
                }
            }
            if(!UserData.CheckIfExists(p)) {
                p.sendMessage("§c§l(!) §7Nie jestes zrejestrowany! Uzyj komendy §8/§fregister")
                return true
            }
            return if(!UserData.Validate(p, args[0], "pass")) {
                p.sendMessage("§c§l(!) §7Zle haslo!")
                true
            } else {
                if(shouldUsePin) {
                    if(!UserData.Validate(p, args[1], "pin")) {
                        p.sendMessage("§c§l(!) §7Zly pin!")
                        return true
                    }
                }
                LoginProcess.removePlayer(p)
                p.sendMessage("§a§l(✔) §7Zalogowano!")
                if(authy.config.getBoolean("SendWelcomeMessage")) {
                    for(message : String in authy.config.getStringList("WelcomeMessage")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
                    }
                }
                authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}Authy${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.YELLOW}Player ${org.bukkit.ChatColor.WHITE}${p.name} ${org.bukkit.ChatColor.YELLOW}logged in with ip ${org.bukkit.ChatColor.WHITE}${p.address?.address?.hostAddress}")
                if(UserData.get(p, "usePin") == "false") {
                    p.sendMessage("§6§l(!) §cNie masz wlaczonego pinu§8! §7Dla bezpieczenstwa ustaw go pod §8/§fpin")
                }
                LoginProcess.EffectRunner.runLogin(p)
                true
            }

        }
        return true
    }
}