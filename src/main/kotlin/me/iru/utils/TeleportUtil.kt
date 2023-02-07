package me.iru.utils

import me.iru.Authy
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

object TeleportUtil {

    private val invalidBlocks = mutableListOf(Material.NETHER_PORTAL)

    fun teleportToValidPlace(player: Player) {
        teleportOutInvalidBlocks(player)
        teleportToGround(player)
    }

    private fun teleportOutInvalidBlocks(player: Player) {
        val loc = player.location
        if(!invalidBlocks.contains(loc.block.type)) return

        var b = loc.block
        for(x in 2 downTo -2 step 1) {
            for(z in 2 downTo -2 step 1) {
                if(invalidBlocks.contains(b.type) && !(b.type.isSolid && b.getRelative(BlockFace.UP).type.isSolid)) {
                    b = b.getRelative(x, 0, z)
                } else {
                    player.teleport(b.location)
                    return
                }
            }
        }
        return
    }

    private fun teleportToGround(player: Player) {
        val authy = Authy.instance

        var loc = player.location
        if(loc.block.getRelative(BlockFace.DOWN).type.isSolid) return

        val mh = loc.world?.minHeight ?: 0

        lateinit var task : BukkitTask
        task = authy.server.scheduler.runTaskTimer(authy, Runnable {
            if(!Authy.loginProcess.contains(player)) {
                task.cancel()
            }

            if(loc.y < mh) {
                task.cancel()
                player.teleport(loc)
            }

            val under = loc.block.getRelative(BlockFace.DOWN)
            if(!under.type.isSolid) {
                loc = under.location
            } else {
                task.cancel()
                player.teleport(loc)
            }
        }, 0L, 0L)
    }
}