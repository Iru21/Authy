package me.mateusz.process

import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class EffectRunner(jplugin : JavaPlugin) {
    val plugin = jplugin
    fun runLogin(p : Player) {
        p.sendTitle("§a§l(✔) §7Zalogowano!", "", 20, 20, 20)
        runFireWorks(p, Color.fromRGB(0, 255, 34))
        p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
        plugin.server.scheduler.runTaskLater(plugin, Runnable {
            p.playSound(p.location, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1F, 1F)
        }, 5L)
    }

    fun runRegister(p : Player) {
        p.sendTitle("${net.md_5.bungee.api.ChatColor.of("#CDFF00")}§l(✔) §7Zarejestrowano!", "", 20, 20, 20)
        runFireWorks(p, Color.fromRGB(205, 255, 0))
        p.playSound(p.location, Sound.BLOCK_TRIPWIRE_CLICK_ON, 1F, 1F)
        p.playSound(p.location, Sound.BLOCK_TRIPWIRE_CLICK_OFF, 1F, 1F)
        plugin.server.scheduler.runTaskLater(plugin, Runnable {
            p.playSound(p.location, Sound.BLOCK_TRIPWIRE_ATTACH, 1F, 1F)
        }, 5L)
    }

    fun runUnregister(p : Player) {
        p.sendTitle("${ChatColor.GOLD}§l(✔) §7Odrejestrowano!", "", 20, 20, 20)
        runFireWorks(p, Color.fromRGB(0xFFAA00))
        p.playSound(p.location, Sound.BLOCK_CHAIN_BREAK, 1F, 1F)
        plugin.server.scheduler.runTaskLater(plugin, Runnable {
            p.playSound(p.location, Sound.BLOCK_CHAIN_BREAK, 1F, 1F)
        }, 5L)
        plugin.server.scheduler.runTaskLater(plugin, Runnable {
            p.playSound(p.location, Sound.BLOCK_CHAIN_PLACE, 1F, 1F)
        }, 5L)
    }

    private fun runFireWorks(p : Player, c : Color) {
        plugin.server.scheduler.runTaskLater(plugin, Runnable {
            val loc = Location(p.world, p.location.x, p.location.y - 1, p.location.z)
            val firework = p.world.spawn(loc, Firework::class.java)
            val fireworkMeta = firework.fireworkMeta
            fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.STAR)
                .with(FireworkEffect.Type.BALL)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.GRAY)
                .withColor(c)
                .withColor(Color.WHITE)
                .build()
            )

            fireworkMeta.power = 0
            firework.fireworkMeta = fireworkMeta
        }, 5L)
    }
}