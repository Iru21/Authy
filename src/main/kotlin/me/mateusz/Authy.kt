package me.mateusz

import me.mateusz.commands.*
import me.mateusz.data.PlayerData
import me.mateusz.events.LoginEvents
import me.mateusz.process.LoginProcess
import me.mateusz.data.Session
import me.mateusz.events.BlockEvents
import me.mateusz.process.runJoin
import me.mateusz.utils.CommandFilter
import me.mateusz.utils.registerCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Authy : JavaPlugin() {
    val version = this.description.version
    val pluginName: String = this.description.name
    val CommandFilter : CommandFilter = CommandFilter()

    companion object {
        lateinit var instance: Authy
        lateinit var translations: Translations
        lateinit var playerData: PlayerData
        lateinit var loginProcess: LoginProcess
        lateinit var session: Session
    }

    override fun onEnable() {

        instance = this
        translations = Translations()
        playerData = PlayerData()
        playerData.init()
        loginProcess = LoginProcess()
        session = Session()

        saveDefaultConfig()

        server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}$pluginName${ChatColor.DARK_GRAY}] ${ChatColor.GREEN}Enabling $version")

        server.pluginManager.registerEvents(LoginEvents(), this)
        server.pluginManager.registerEvents(BlockEvents(), this)

        registerCommand(cRegister())
        registerCommand(cLogin())
        registerCommand(cUnregister())
        registerCommand(cRemember())
        registerCommand(cAuthy())
        registerCommand(cPin())

        with(CommandFilter) { registerFilter() }

        val players = server.onlinePlayers
        for(player : Player in players) {
            runJoin(player)
        }

    }

    override fun onDisable() {
        server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}$pluginName${ChatColor.DARK_GRAY}] ${ChatColor.RED}Disabling $version")
    }

}