package me.mateusz

import me.mateusz.commands.cLogin
import me.mateusz.commands.cRegister
import me.mateusz.commands.cRemember
import me.mateusz.commands.cUnregister
import me.mateusz.events.LoginEvents
import me.mateusz.process.LoginProcess
import me.mateusz.utils.CommandFilter
import me.mateusz.utils.registerCommand
import org.apache.logging.log4j.core.filter.AbstractFilter
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Filter

class Authy : JavaPlugin(), Listener {
    val version = this.description.version
    val pluginName: String = this.description.name
    val LoginProcess : LoginProcess = LoginProcess(this)
    val CommandFilter : CommandFilter = CommandFilter()

    override fun onEnable() {
        server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}$pluginName${ChatColor.DARK_GRAY}] ${ChatColor.GREEN}Enabling $version")

        server.pluginManager.registerEvents(LoginEvents(this, LoginProcess), this)

        registerCommand(this, cRegister("register", this, LoginProcess))
        registerCommand(this, cLogin("login", this, LoginProcess))
        registerCommand(this, cUnregister("unregister", this, LoginProcess))
        registerCommand(this, cRemember("remember", this))

        with(CommandFilter) { registerFilter() }

    }

    override fun onDisable() {
        server.consoleSender.sendMessage("${ChatColor.DARK_GRAY}[${ChatColor.GOLD}$pluginName${ChatColor.DARK_GRAY}] ${ChatColor.RED}Disabling $version")
    }

}