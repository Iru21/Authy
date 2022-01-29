package me.mateusz.commands

import me.mateusz.Authy
import me.mateusz.interfaces.ICommand
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File


class cAuthy(override var name: String = "authy") : ICommand {
    val authy = Authy.instance
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            if(args.size != 1) {
                p.sendMessage("§8[§6Authy§8] §7Created by: §cIru21 §8- §7https://github.com/Iru21/")
                return true
            } else if(args[0].lowercase() == "reload") {
                val file = File(authy.dataFolder.absolutePath + "/config.yml")
                authy.config.load(file)
                p.sendMessage("§8[§6Authy§8] §7Reloaded §cconfig§7!")
                p.playSound(p.location, Sound.BLOCK_CHAIN_PLACE, 1F, 1F)
            }
        }
        return true
    }
}