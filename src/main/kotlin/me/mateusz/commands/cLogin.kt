package me.mateusz.commands

import me.mateusz.interfaces.ICommand
import me.mateusz.process.LoginProcess
import me.mateusz.process.UserData
import me.mateusz.utils.HashUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class cLogin(override var name: String, jplugin : JavaPlugin, preLoginProcess : LoginProcess) : ICommand {
    val plugin : JavaPlugin = jplugin
    val UserData : UserData = UserData(plugin)
    val LoginProcess : LoginProcess = preLoginProcess
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            if(!LoginProcess.checkIfContains(p)) {
                p.sendMessage("§c§l(!) §7Jestes juz uwierzytelniony!")
                return true
            }
            if(args.size != 1) {
                p.sendMessage("§c§l(!) §7Uzycie: §8/§flogin §8[§fhaslo§8]")
                return true
            }
            if(!UserData.CheckIfExists(p)) {
                p.sendMessage("§c§l(!) §7Nie jestes zrejestrowany! Uzyj komendy §8/§fregister")
                return true
            }
            return if(!UserData.Validate(p, args[0])) {
                p.sendMessage("§c§l(!) §7Zle haslo!")
                true
            } else {
                LoginProcess.removePlayer(p)
                p.sendMessage("§a§l(✔) §7Zalogowano!")
                LoginProcess.EffectRunner.runLogin(p)
                true
            }

        }
        return true
    }
}