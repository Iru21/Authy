package me.iru

import me.iru.commands.*
import me.iru.data.PlayerData
import me.iru.data.Session
import me.iru.data.migration.DatabaseMigration
import me.iru.data.migration.Migration
import me.iru.events.BlockEvents
import me.iru.events.LoginEvents
import me.iru.process.JoinProcess
import me.iru.process.LoginProcess
import me.iru.utils.CommandFilter
import me.iru.utils.isNewVersionAvailable
import me.iru.utils.registerCommand
import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin


class Authy : JavaPlugin() {
    val version = this.description.version
    var latestVersion = this.version

    private val pluginName = this.description.name
    private val commandFilter = CommandFilter()

    val prefix = "${ChatColor.DARK_GRAY}[${ChatColor.GOLD}$pluginName${ChatColor.DARK_GRAY}]"

    companion object {
        lateinit var instance: Authy private set
        lateinit var translations: Translations private set
        lateinit var playerData: PlayerData private set
        lateinit var loginProcess: LoginProcess private set
        lateinit var session: Session private set
        lateinit var authManager: AuthManager private set
    }

    override fun onEnable() {
        instance = this

        translations = Translations()
        playerData = PlayerData()
        Migration.updateSystem()
        DatabaseMigration.tryMigrate()
        loginProcess = LoginProcess()
        authManager = AuthManager()
        session = Session()

        saveDefaultConfig()
        config.options().copyDefaults(true)
        saveConfig()

        server.pluginManager.registerEvents(LoginEvents(), this)
        server.pluginManager.registerEvents(BlockEvents(), this)

        registerCommand(cRegister())
        registerCommand(cLogin())
        registerCommand(cUnregister())
        registerCommand(cRemember())
        registerCommand(cAuthy())
        registerCommand(cPin())
        registerCommand(cChangePassword())

        with(commandFilter) { registerFilter() }

        server.consoleSender.sendMessage("$prefix ${ChatColor.GREEN}Enabled $version")

        val players = server.onlinePlayers
        for(player : Player in players) {
            JoinProcess(player).run()
        }

        this.server.scheduler.runTaskAsynchronously(this, Runnable {
            val v = isNewVersionAvailable()
            if(v.first) {
                this.latestVersion = v.second
                server.consoleSender.sendMessage("$prefix ${ChatColor.YELLOW}New version available - ${ChatColor.GREEN}${this.latestVersion}${ChatColor.YELLOW}!")
            }
        })

        setupMetrics()
    }

    override fun onDisable() {
        DatabaseMigration.saveLastDatabaseType()
        playerData.databaseConnection.shutdownConnections()
        server.consoleSender.sendMessage("$prefix ${ChatColor.RED}Disabled $version")
    }

    private fun setupMetrics() {
        val metrics = Metrics(this, 14475)

        metrics.addCustomChart(SimplePie("db_type") {
            playerData.databaseConnection.type.name
        })
        metrics.addCustomChart(SimplePie("lang") {
            instance.config.getString("lang")
        })
    }

}