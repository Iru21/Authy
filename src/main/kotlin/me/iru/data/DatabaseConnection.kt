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

    private val connectionPool = mutableListOf<Connection>()
    private val usedConnections = mutableListOf<Connection>()
    private val maxPoolSize = 10

    init {
        for(i in 0..maxPoolSize) {
            connectionPool.add(createConnection())
        }
    }

    private fun createConnection(): Connection {
        return when(type) {
            DatabaseType.MySQL -> connectMySQL()
            DatabaseType.SQLite -> connectSQLite()
        }
    }

    private fun connectSQLite(): Connection {
        Class.forName("org.sqlite.JDBC");

        if(!authy.dataFolder.exists()) {
            authy.dataFolder.mkdir()
        }

        return DriverManager.getConnection("jdbc:sqlite:${authy.dataFolder}${File.separator}data.db")
    }

    private fun connectMySQL(): Connection {
        val host = authy.config.getString("database.credentials.host")
        val user = authy.config.getString("database.credentials.user")
        val password = authy.config.getString("database.credentials.password")
        val databaseName = authy.config.getString("database.credentials.database")

        Class.forName("com.mysql.cj.jdbc.Driver")

        val t = DriverManager
            .getConnection("jdbc:mysql://$host", user, password)
        t.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS $databaseName")
        t.close()



        return DriverManager.getConnection("jdbc:mysql://$host/$databaseName", user, password)
    }

    private fun getConnection(): Connection {
        if(connectionPool.isEmpty()) {
            if(usedConnections.size < maxPoolSize) {
                connectionPool.add(createConnection())
            } else {
                throw RuntimeException("Maximum pool size reached, no available connections!")
            }
        }
        var c = connectionPool.removeAt(connectionPool.size - 1)
        if(!c.isValid(3600)) c = createConnection()
        usedConnections.add(c)
        return c
    }

    fun query(q: String): ResultSet? {
        val c = getConnection()
        val s = c.prepareStatement(q)
        var r: ResultSet? = null
        if(s.execute()) {
            r = s.resultSet
        }
        releaseConnection(c)
        return r
    }

    fun query(list: MutableList<String>) {
        val c = getConnection()
        val s = c.createStatement()
        for(entry in list) {
            s.addBatch(entry)
        }
        s.executeBatch()
        releaseConnection(c)
    }

    private fun releaseConnection(c: Connection) {
        connectionPool.add(c)
        usedConnections.remove(c)
    }

    fun shutdownConnections() {
        usedConnections.forEach {
            releaseConnection(it)
        }
        connectionPool.forEach {
            it.close()
        }
        connectionPool.clear()
    }

}