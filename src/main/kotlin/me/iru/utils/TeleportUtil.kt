package me.iru.utils

import me.iru.Authy
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

object TeleportUtil {
    fun teleportToValidPlace(player: Player) {
        teleportToGround(player)
    }

    fun teleportToGround(player: Player) {
        val authy = Authy.instance

        var f = false
        if(player.isFlying) {
            f = true
            player.isFlying = false
        }

        var task0 : BukkitTask? = null
        var loc = player.location
        task0 = authy.server.scheduler.runTaskTimer(authy, Runnable {
            if(!loc.block.getRelative(BlockFace.DOWN).type.isSolid) {
                loc = Location(loc.world, loc.x, loc.y - 1, loc.z)
            } else {
                task0!!.cancel()
                player.teleport(loc)
            }
        }, 0L, 0L)

        if(f) player.isFlying = true
    }
}