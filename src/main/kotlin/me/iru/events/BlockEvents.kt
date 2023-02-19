package me.iru.events

import me.iru.Authy
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.player.*

class BlockEvents : Listener {
    val loginProcess = Authy.loginProcess

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onMove(e : PlayerMoveEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockBreak(e : BlockBreakEvent) {
        if (loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockPlace(e : BlockPlaceEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onItemConsume(e : PlayerItemConsumeEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onItemDrop(e : PlayerDropItemEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onUse(e : PlayerInteractEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityUse(e : PlayerInteractAtEntityEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityUse2(e : PlayerInteractEntityEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamageTaken(e : EntityDamageEvent) {
        if(e.entity is Player) {
            if (loginProcess.contains((e.entity as Player).player as Player)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onFoodLevelChange(e : FoodLevelChangeEvent) {
        if(e.entity is Player) {
            if (loginProcess.contains((e.entity as Player).player as Player)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onAttack(e : EntityDamageByEntityEvent) {
        if(e.damager is Player) {
            if (loginProcess.contains(e.damager as Player)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onAnyCommand(e : PlayerCommandPreprocessEvent) {
        val allowedCommands = mutableListOf("/l", "/login", "/reg", "/register")
        if(loginProcess.contains(e.player)) {
            if (!allowedCommands.contains(e.message.split(" ")[0])) {
                e.isCancelled = true
                loginProcess.sendPleaseAuthMessage(e.player)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(e : AsyncPlayerChatEvent)  {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
            loginProcess.sendPleaseAuthMessage(e.player)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryInteract(e : InventoryInteractEvent) {
        val p = e.whoClicked as Player
        if(loginProcess.contains(p)) {
            e.isCancelled = true
            loginProcess.sendPleaseAuthMessage(p)
        }
    }
}