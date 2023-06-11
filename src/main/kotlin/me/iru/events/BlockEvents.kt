package me.iru.events

import me.iru.Authy
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.player.*


class BlockEvents : Listener {
    val loginProcess = Authy.loginProcess

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onMove(e : PlayerMoveEvent) {
        if(loginProcess.contains(e.player)) {
            val from = e.from
            val to = e.to ?: return

            if (from.blockX == to.blockX &&
                from.blockZ == to.blockZ &&
                from.y - to.y >= 0
            ) return

            e.setTo(from)
        }
    }

    @EventHandler
    fun onBlockBreak(e : BlockBreakEvent) {
        if (loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(e : BlockPlaceEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onItemConsume(e : PlayerItemConsumeEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onItemDrop(e : PlayerDropItemEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onItemHeld(e : PlayerItemHeldEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onItemPickup(e : EntityPickupItemEvent) {
        if(e.entity is Player) {
            if (loginProcess.contains((e.entity as Player).player as Player)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onUse(e : PlayerInteractEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityUse(e : PlayerInteractAtEntityEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityUse2(e : PlayerInteractEntityEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDamageTaken(e : EntityDamageEvent) {
        if(e.entity is Player) {
            if (loginProcess.contains((e.entity as Player).player as Player)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onFoodLevelChange(e : FoodLevelChangeEvent) {
        if(e.entity is Player) {
            if (loginProcess.contains((e.entity as Player).player as Player)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onAttack(e : EntityDamageByEntityEvent) {
        if(e.damager is Player) {
            if (loginProcess.contains(e.damager as Player)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onAnyCommand(e : PlayerCommandPreprocessEvent) {
        val allowedCommands = mutableListOf("/l", "/login", "/reg", "/register")
        if(loginProcess.contains(e.player)) {
            if (!allowedCommands.contains(e.message.split(" ")[0])) {
                e.isCancelled = true
                loginProcess.sendPleaseAuthMessage(e.player)
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChat(e : AsyncPlayerChatEvent)  {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
            loginProcess.sendPleaseAuthMessage(e.player)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryInteract(e : InventoryInteractEvent) {
        val p = e.whoClicked as Player
        if(loginProcess.contains(p)) {
            e.isCancelled = true
            loginProcess.sendPleaseAuthMessage(p)
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    fun onShear(e : PlayerShearEntityEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onFish(e : PlayerFishEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onBedEnter(e : PlayerBedEnterEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEditBook(e : PlayerEditBookEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onSignChange(e : SignChangeEvent) {
        if(loginProcess.contains(e.player)) {
            e.isCancelled = true
        }
    }
}