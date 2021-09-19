package me.mateusz.commands

import me.mateusz.interfaces.ICommand
import me.mateusz.process.UserData
import me.mateusz.utils.HashUtil
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class cPin(override var name: String, jplugin : JavaPlugin) : ICommand {
    val plugin = jplugin
    val UserData : UserData = UserData(plugin)
    val HashUtil : HashUtil = HashUtil()
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender is Player) {
            val p : Player = sender
            UserData.updateIfOld(p, "usePin", false)
            UserData.updateIfOld(p, "pin", "not_set")
            if(args.isEmpty()) {
                val status = if(UserData.get(p, "usePin") == "true") "§aWlaczony" else "§cWylaczony"
                p.sendMessage("§8[§6●§8] §8§m----§r §7Pin §8§m----§r §8[§6●§8]")
                p.sendMessage("")
                p.sendMessage("§8  - §7Status§8: $status")
                p.sendMessage("")
                p.sendMessage("    §8§m----")
                p.sendMessage("")
                p.sendMessage("§8  - /§fpin §7toggle §8- §7Wlacza/wylacza pin")
                p.sendMessage("§8  - /§fpin §7set §8[§7nowy pin§8] - §7Ustawia pin")
                p.sendMessage("")
                p.sendMessage("§8[§6●§8] §8§m----§r §7Pin §8§m----§r §8[§6●§8]")
            } else if(args[0].lowercase() == "toggle") {
                if(UserData.get(p, "pin") == "not_set") {
                    p.sendMessage("§c§l(!) §7Ustaw najpierw pin! §8/§fpin §7set §8[§7nowy pin§8]")
                    return true
                }
                val current = UserData.get(p, "usePin")
                when(current) {
                    "true" -> {
                        UserData.set(p, "usePin", false)
                        p.sendMessage("§a§l(✔) §cWylaczono §7pin§8!")
                    }
                    else -> {
                        UserData.set(p, "usePin", true)
                        p.sendMessage("§a§l(✔) §aWlaczono §7pin§8!")
                    }

                }
            }else if(args[0].lowercase() == "set") {
                if(args.size != 2) {
                    p.sendMessage("§c§l(!) §7Uzycie§8: /§fpin §7set §8[§7nowy pin§8]")
                    return true
                }
                if(!UserData.PinMatchesRules(args[1])) {
                    p.sendMessage("§c§l(!) §7Pin musis skladac sie z 6 cyfr§8!")
                    return true
                }
                UserData.set(p, "pin", HashUtil.toSHA256(args[1]))
                p.sendMessage("§a§l(✔) §7Ustawiono pin§8! §7Nie zapomnij wlaczyc go uzywajac §8/§fpin §7toggle")
            }
        }
        return true
    }
}