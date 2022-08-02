package me.iru.data

import me.iru.Authy
import net.md_5.bungee.api.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.PrintWriter

object Migration {
    val authy = Authy.instance
    val playerData = Authy.playerData

    private fun logMigration(msg: String) {
        authy.server.consoleSender.sendMessage("${authy.prefix} ${ChatColor.DARK_GREEN}$msg")
    }

    fun saveLastDatabaseType() {
        // File#writeText was causing NoClassDefFoundError on Kotlin/Unit for some f reason :|
        val lastdb = File(authy.dataFolder, "lastdb")
        val w = PrintWriter(lastdb)
        w.println(playerData.databaseConnection.type.toString())
        w.close()
    }

    fun getLastDatabaseType(): String? {
        val lastdb = File(authy.dataFolder, "lastdb")
        return if(lastdb.exists()) {
            lastdb.readText()
        }
        else null
    }

    fun migrateDatabase() {
        var lasttype = getLastDatabaseType()
        val currenttype = playerData.databaseConnection.type.toString()
        if(lasttype != currenttype && lasttype != null) {
            logMigration("Migrating all data from $lasttype database to $currenttype database... (This might take a while depending on the size of your database)")
            lasttype = lasttype.trim()
            val lastconn = DatabaseConnection(DatabaseType.valueOf(lasttype))

            val d = lastconn.query("SELECT * FROM players")!!
            val all = HashSet<AuthyPlayer>()
            if(d.next()) {
                do {
                    all.add(playerData.construct(d))
                } while(d.next())
            }

            if(all.isEmpty()) {
                logMigration("Found nothing, aborting...")
                return
            }

            if(playerData.databaseConnection.type == DatabaseType.MySQL) playerData.databaseConnection.query("TRUNCATE players")
            else if(playerData.databaseConnection.type == DatabaseType.SQLite) playerData.databaseConnection.query("DELETE FROM players")
            val list = mutableListOf<String>()
            for(p in all) {
                list.add("INSERT INTO players(uuid, username, ip, password, isPinEnabled, pin, session) VALUES(" +
                        "'${p.uuid}', " +
                        "'${p.username}', " +
                        "'${p.ip}', " +
                        "'${p.password}', " +
                        "'${if(p.isPinEnabled) 1 else 0}', " +
                        "'${p.pin}', " +
                        "'${p.session}')")
            }
            playerData.databaseConnection.queryBatch(list)

            lastconn.killConnection()

        }
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
                            data.getString("pin")!!,
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
                            data.getString("hashedPin")!!,
                            data.getLong("session")
                        )
                    )
                }
            }

            dataFile.delete()
        }
    }
}