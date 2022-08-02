package me.iru.data

import me.iru.Authy
import me.iru.utils.HashUtil
import org.bukkit.entity.Player
import java.sql.ResultSet
import java.util.*
import kotlin.collections.HashSet

class PlayerData {
    val authy = Authy.instance
    val databaseConnection: DatabaseConnection

    init {
        val cfType = authy.config.getString("database.type")!!
        databaseConnection = DatabaseConnection(
            when(cfType.lowercase()) {
                "mysql" -> DatabaseType.MySQL
                else -> DatabaseType.SQLite
            }
        )

        databaseConnection.query("CREATE TABLE IF NOT EXISTS players(" +
                "uuid VARCHAR(36) NOT NULL PRIMARY KEY," +
                "username VARCHAR(16) NOT NULL," +
                "ip VARCHAR(16) NOT NULL," +
                "password VARCHAR(64) NOT NULL," +
                "isPinEnabled TINYINT(1) NOT NULL DEFAULT '0'," +
                "pin VARCHAR(64)," +
                "session BIGINT(20) NOT NULL DEFAULT '0'" +
                ")")
    }

    fun create(p: Player, password: String) {
        if(!exists(p.uniqueId)) {
            update(
                AuthyPlayer(
                    p.uniqueId,
                    p.name,
                    p.address?.address?.hostAddress!!,
                    HashUtil.toSHA256(password)
                )
            )
        }
    }

    fun getAll(): HashSet<AuthyPlayer> {
        val d = databaseConnection.query("SELECT * FROM players")!!
        val set = HashSet<AuthyPlayer>()
        if(d.next()) {
            do {
                set.add(construct(d))
            } while(d.next())
        }
        return set
    }

    fun delete(uuid: UUID): Boolean {
        return if(!exists(uuid)) false
        else {
            databaseConnection.query("DELETE FROM players WHERE uuid = '${uuid}'")
            true
        }
    }

    fun get(uuid: UUID): AuthyPlayer? {
        val d = databaseConnection.query("SELECT * FROM players WHERE uuid = '${uuid}'")!!
        return if(!d.isBeforeFirst) null
        else {
            d.next()
            construct(d)
        }
    }

    fun update(d: AuthyPlayer) {
        var dup = ""
        if(databaseConnection.type == DatabaseType.MySQL) dup = "DUPLICATE KEY UPDATE"
        else if(databaseConnection.type == DatabaseType.SQLite) dup = "CONFLICT(uuid) DO UPDATE SET"
        databaseConnection.query(
            "INSERT INTO players(uuid, username, ip, password, isPinEnabled, pin, session) VALUES(" +
               "'${d.uuid}', " +
               "'${d.username}', " +
               "'${d.ip}', " +
               "'${d.password}', " +
               "'${if(d.isPinEnabled) 1 else 0}', " +
               "'${d.pin}', " +
               "'${d.session}')" +
                "ON $dup username='${d.username}', ip='${d.ip}', password='${d.password}', isPinEnabled='${if(d.isPinEnabled) 1 else 0}', pin='${d.pin}', session='${d.session}'")

    }

    fun exists(uuid: UUID): Boolean {
        val d = databaseConnection.query("SELECT * FROM players WHERE uuid = '${uuid}'")!!
        return d.isBeforeFirst
    }

    fun construct(d: ResultSet): AuthyPlayer {
        return AuthyPlayer(
            UUID.fromString(d.getString("uuid")),
            d.getString("username"),
            d.getString("ip"),
            d.getString("password"),
            d.getBoolean("isPinEnabled"),
            d.getString("pin"),
            d.getLong("session")
        )
    }
}