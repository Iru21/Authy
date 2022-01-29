package me.mateusz.events

import me.mateusz.Authy
import me.mateusz.process.runJoin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.*
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent


class LoginEvents : Listener {

    val LoginProcess = Authy.loginProcess
    val authy = Authy.instance

    @EventHandler
    fun onJoin(e : PlayerJoinEvent) {
        runJoin(e.player)
    }

    @EventHandler
    fun onQuit(e : PlayerQuitEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            LoginProcess.removePlayer(e.player)
        }
    }

    @EventHandler
    fun onMove(e : PlayerMoveEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockBreak(e : BlockBreakEvent) {
        if (LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(e : BlockPlaceEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onItemConsume(e : PlayerItemConsumeEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onItemDrop(e : PlayerDropItemEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onUse(e : PlayerInteractEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityUse(e : PlayerInteractAtEntityEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityUse2(e : PlayerInteractEntityEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onDamageTaken(e : EntityDamageEvent) {
        if(e.entity is Player) {
            if (LoginProcess.checkIfContains((e.entity as Player).player as Player)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onAttack(e : EntityDamageByEntityEvent) {
        if(e.damager is Player) {
            if (LoginProcess.checkIfContains(e.damager as Player)) {
                e.isCancelled = true
            }
        }
    }


    @EventHandler
    fun onAnyCommand(e : PlayerCommandPreprocessEvent) {
        val allowedCommands = mutableListOf<String>("/l", "/login", "/reg", "/register")
        if(LoginProcess.checkIfContains(e.player)) {
            if (!allowedCommands.contains(e.message.split(" ")[0])) {
                e.isCancelled = true
                LoginProcess.sendPleaseAuthMessage(e.player)
            }
        }
    }

    @EventHandler
    fun onChat(e : AsyncPlayerChatEvent)  {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
            LoginProcess.sendPleaseAuthMessage(e.player)
        }
    }
}