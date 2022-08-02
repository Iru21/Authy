package me.iru.data

import me.iru.Authy
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

enum class DatabaseType {
    MySQL,
    SQLite
}

class DatabaseConnection(val type: DatabaseType) {

    private val authy = Authy.instance

    private var conn: Connection = when(type) {
        DatabaseType.MySQL -> connectMySQL()
        DatabaseType.SQLite -> connectSQLite()
    }

    fun connectSQLite(): Connection {
        return DriverManager.getConnection("jdbc:sqlite:${authy.dataFolder}${File.separator}data.db")
    }

    fun connectMySQL(): Connection {
        val host = authy.config.getString("database.credentials.host")
        val user = authy.config.getString("database.credentials.user")
        val password = authy.config.getString("database.credentials.password")
        val databaseName = authy.config.getString("database.credentials.database")

        val t = DriverManager
            .getConnection("jdbc:mysql://$host", user, password)
        t.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS $databaseName")
        t.close()

        return DriverManager.getConnection("jdbc:mysql://$host/$databaseName", user, password)
    }

    fun query(q: String): ResultSet? {
        val s = conn.createStatement()
        return if(s.execute(q)) {
            s.resultSet
        }
        else null
    }

    fun queryBatch(list: MutableList<String>) {
        val s = conn.createStatement()
        for(entry in list) {
            s.addBatch(entry)
        }
        s.executeBatch()
    }

    fun killConnection() {
        conn.close()
    }

}