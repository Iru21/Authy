package me.iru.events

import me.iru.Authy
import me.iru.process.DuplicateProtection
import me.iru.process.JoinProcess
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.*

class LoginEvents : Listener {

    val loginProcess = Authy.loginProcess
    val authy = Authy.instance

    @EventHandler
    fun onJoin(e : PlayerJoinEvent) {
        if(DuplicateProtection.check(e.player)) {
            JoinProcess(e.player).run()
        }
    }

    @EventHandler
    fun onQuit(e : PlayerQuitEvent) {
        if (loginProcess.contains(e.player)) {
            loginProcess.removePlayer(e.player)
        }
    }
}