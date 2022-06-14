package me.iru.events

import me.iru.Authy
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.*

class BlockEvents : Listener {
    val loginProcess = Authy.loginProcess

    @EventHandler
    fun onMove(e : PlayerMoveEvent) {
        if(loginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockBreak(e : BlockBreakEvent) {
        if (loginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(e : BlockPlaceEvent) {
        if(loginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onItemConsume(e : PlayerItemConsumeEvent) {
        if(loginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onItemDrop(e : PlayerDropItemEvent) {
        if(loginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onUse(e : PlayerInteractEvent) {
        if(loginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityUse(e : PlayerInteractAtEntityEvent) {
        if(loginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityUse2(e : PlayerInteractEntityEvent) {
        if(loginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onDamageTaken(e : EntityDamageEvent) {
        if(e.entity is Player) {
            if (loginProcess.checkIfContains((e.entity as Player).player as Player)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onAttack(e : EntityDamageByEntityEvent) {
        if(e.damager is Player) {
            if (loginProcess.checkIfContains(e.damager as Player)) {
                e.isCancelled = true
            }
        }
    }


    @EventHandler
    fun onAnyCommand(e : PlayerCommandPreprocessEvent) {
        val allowedCommands = mutableListOf("/l", "/login", "/reg", "/register")
        if(loginProcess.checkIfContains(e.player)) {
            if (!allowedCommands.contains(e.message.split(" ")[0])) {
                e.isCancelled = true
                loginProcess.sendPleaseAuthMessage(e.player)
            }
        }
    }

    @EventHandler
    fun onChat(e : AsyncPlayerChatEvent)  {
        if(loginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
            loginProcess.sendPleaseAuthMessage(e.player)
        }
    }
}