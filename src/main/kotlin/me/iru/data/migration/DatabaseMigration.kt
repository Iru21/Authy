package me.iru.data.migration

import me.iru.Authy
import me.iru.data.AuthyPlayer
import me.iru.data.DatabaseConnection
import me.iru.data.DatabaseType
import java.io.File
import java.io.PrintWriter

object DatabaseMigration {

    val authy = Authy.instance
    val playerData = Authy.playerData

    fun tryMigrate() {
        val lasttype = getLastDatabaseType()
        val currenttype = playerData.databaseConnection.type.toString()
        if(lasttype != currenttype && lasttype != null) {
            Migration.logMigration("Migrating all data from $lasttype database to $currenttype database... (This might take a while depending on the size of your database)")
            val lastconn = DatabaseConnection(DatabaseType.valueOf(lasttype))

            val d = lastconn.query("SELECT * FROM players")!!
            val all = HashSet<AuthyPlayer>()
            if(d.next()) {
                do {
                    all.add(playerData.construct(d))
                } while(d.next())
            }

            if(all.isEmpty()) {
                Migration.logMigration("Found nothing, aborting...")
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
            playerData.databaseConnection.query(list)

            lastconn.shutdownConnections()

        }
    }

    fun saveLastDatabaseType() {
        // File#writeText was causing NoClassDefFoundError on Kotlin/Unit for some f reason :|
        val lastdb = File(Migration.authy.dataFolder, "lastdb")
        val w = PrintWriter(lastdb)
        w.print(Migration.playerData.databaseConnection.type.toString())
        w.close()
    }

    fun getLastDatabaseType(): String? {
        val lastdb = File(Migration.authy.dataFolder, "lastdb")
        return if(lastdb.exists()) {
            lastdb.readText()
        }
        else null
    }

}