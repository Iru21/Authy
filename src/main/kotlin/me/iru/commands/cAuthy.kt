package me.iru.commands

import me.iru.Authy
import me.iru.interfaces.ICommand
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

class cAuthy(override var name: String = "authy") : ICommand {
    val authy = Authy.instance
    val translations = Authy.translations
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(args.size != 1) {
            sender.sendMessage("§8[§6Authy§8] §7Created by: §cIru21 §8- §7https://github.com/Iru21/")
            return true
        } else if(args[0].lowercase() == "reload") {
            if(sender.hasPermission("authy.reload")) {
                val file = File(authy.dataFolder.absolutePath + "/config.yml")
                authy.config.load(file)
                sender.sendMessage("§8[§6Authy§8] §7Reloaded §cconfig§7!")
                translations.updateCache()
                sender.sendMessage("§8[§6Authy§8] §7Reloaded §ctranslations§7!")
                if (sender is Player) {
                    val p: Player = sender
                    p.playSound(p.location, Sound.BLOCK_CHAIN_PLACE, 1F, 1F)
                }
            }
        } else if(args[0].lowercase() == "info") {
            if(sender.hasPermission("authy.info")) {
                val registered = Authy.playerData.getAll().size
                val dbtype = Authy.playerData.databaseConnection.type
                val dpl = authy.config.getInt("duplicateIpProtection.protectionLevel")

                sender.sendMessage("§8[§6Authy§8] &8&m----&r &7Info &8&m----&r §8[§6Authy§8]")
                sender.sendMessage("")
                sender.sendMessage("§8  - §7Registered Players§8: §6$registered")
                sender.sendMessage("§8  - §7Database Type§8: §6$dbtype")
                sender.sendMessage("§8  - §7Duplicate Protection Level§8: §6$dpl")
                sender.sendMessage("")
                sender.sendMessage("§8[§6Authy§8] &8&m----&r &7Info &8&m----&r §8[§6Authy§8]")
            }
        }
        return true
    }
}