package me.mateusz.events

import me.mateusz.Authy
import me.mateusz.process.DuplicateProtection
import me.mateusz.process.runJoin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.*

class LoginEvents : Listener {

    val loginProcess = Authy.loginProcess
    val authy = Authy.instance

    @EventHandler
    fun onJoin(e : PlayerJoinEvent) {
        if(DuplicateProtection.check(e.player)) {
            runJoin(e.player)
        }
    }

    @EventHandler
    fun onQuit(e : PlayerQuitEvent) {
        if (loginProcess.checkIfContains(e.player)) {
            loginProcess.removePlayer(e.player)
        }
    }
}