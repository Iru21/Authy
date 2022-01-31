package me.mateusz.commands

import me.mateusz.Authy
import me.mateusz.interfaces.ICommand
import me.mateusz.process.LoginProcess
import me.mateusz.process.UserData
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class cRegister(override var name: String = "register") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    val UserData : UserData = UserData()
    val LoginProcess : LoginProcess = Authy.loginProcess
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            if(!LoginProcess.checkIfContains(p)) {
                p.sendMessage("§c§l(!) ${translations.get("already_authed")}!")
                return true
            }
            if(args.size != 2) {
                p.sendMessage("§c§l(!) ${translations.get("command_register_usage")}")
                return true
            }
            if(args[0] != args[1]) {
                p.sendMessage("§c§l(!) ${translations.get("command_register_notidentical")}")
                return true
            }
            if(!UserData.PasswordMatchesRules(args[0])) {
                p.sendMessage("§c§l(!) ${translations.get("command_register_breaksrules")}")
                return true
            }
            return if(!UserData.CheckIfExists(p)) {
                UserData.CreateOrGetUser(p, args[0])
                LoginProcess.removePlayer(p)
                p.sendMessage("${net.md_5.bungee.api.ChatColor.of("#CDFF00")}§l(✔) ${translations.get("register_success")}")
                if(authy.config.getBoolean("SendWelcomeMessage")) {
                    for(message : String in authy.config.getStringList("WelcomeMessage")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
                    }
                }
                authy.server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}Authy${ChatColor.DARK_GRAY}] ${ChatColor.YELLOW}Player ${ChatColor.WHITE}${p.name} ${ChatColor.YELLOW}registered with ip ${ChatColor.WHITE}${p.address?.address?.hostAddress}")
                if(UserData.get(p, "usePin") == "false") {
                    p.sendMessage("§6§l(!) ${translations.get("no_pin_warning")}")
                }
                LoginProcess.EffectRunner.runRegister(p)
                true
            } else {
                p.sendMessage("§c§l(!) ${translations.get("command_register_alreadyregistered")}")
                true
            }
        }
        return true
    }
}