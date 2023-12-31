package me.iru.data.migration

import me.iru.Authy
import me.iru.data.AuthyPlayer
import net.md_5.bungee.api.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

object Migration {
    val authy = Authy.instance
    val playerData = Authy.playerData

    fun logMigration(msg: String) {
        authy.server.consoleSender.sendMessage("${authy.prefix} ${ChatColor.DARK_GREEN}$msg")
    }

    fun updateSystem() {
        val userdata = File(authy.dataFolder, "userdata" + File.separator)
        if(userdata.exists()) {
            logMigration("Migrating userdata folder to player-data...")
            userdata.renameTo(File(authy.dataFolder, "player-data"))
        }

        val playerData = File(authy.dataFolder, "player-data")
        if(playerData.list()?.isEmpty() == true) {
            logMigration("Deleting empty player-data folder...")
            playerData.delete()
        }
    }

    fun updatePlayer(p: Player) {
        val dataFile = File(authy.dataFolder, "player-data${File.separator}${p.uniqueId}.yml")
        if(dataFile.exists()) {
            val data = YamlConfiguration.loadConfiguration(dataFile)
            when(data.getInt("version")) {
                0 -> { // key not found in data file
                    logMigration("Migrating userdata player file...")
                    playerData.update(
                        AuthyPlayer(
                            p.uniqueId,
                            data.getString("usr")!!,
                            data.getString("ip")!!,
                            data.getString("pass")!!,
                            data.getBoolean("usePin"),
                            if(data.getString("pin") == "not_set") null else data.getString("pin"),
                            data.getLong("session")
                        )
                    )
                }
                4 -> {
                    logMigration("Migrating player-data (V4) player file...")
                    playerData.update(
                        AuthyPlayer(
                            p.uniqueId,
                            data.getString("username")!!,
                            data.getString("ip")!!,
                            data.getString("hashedPassword")!!,
                            data.getBoolean("usePin"),
                            data.getString("hashedPin"),
                            data.getLong("session")
                        )
                    )
                }
            }

            dataFile.delete()
        }


    }
}