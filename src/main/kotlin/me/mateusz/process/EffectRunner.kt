package me.mateusz.process

import me.mateusz.Authy
import me.mateusz.PrefixType
import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player

class EffectRunner {
    val authy = Authy.instance
    val translations = Authy.translations
    val config = Authy.instance.config
    fun runLogin(p : Player) {
        val shouldShowTitle = config.getBoolean("effects.title")
        val shouldShowParticles = config.getBoolean("effects.particles")
        val shouldPlaySound = config.getBoolean("effects.sounds")
        if(shouldShowTitle) p.sendTitle("${translations.getPrefix(PrefixType.LOGIN)} ${translations.get("login_success")}", "", 20, 20, 20)
        if (shouldShowParticles) {
            val c = translations.getColor("prefix_login_color").color
            runFireWorks(p, Color.fromRGB(c.red, c.green, c.blue))
        }
        if(shouldPlaySound) {
            p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
            authy.server.scheduler.runTaskLater(authy, Runnable {
                p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
                p.playSound(p.location, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1F, 1F)
            }, 5L)
            authy.server.scheduler.runTaskLater(authy, Runnable {
                p.playSound(p.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F)
                p.playSound(p.location, Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1F, 1F)
            }, 10L)
        }
    }

    fun runAutoLogin(p : Player) {
        val shouldShowTitle = config.getBoolean("effects.title")
        val shouldShowParticles = config.getBoolean("effects.particles")
        val shouldPlaySound = config.getBoolean("effects.sounds")
        if(shouldShowTitle) p.sendTitle("${translations.getPrefix(PrefixType.LOGIN)} ${translations.get("autologin_success")}", "", 20, 20, 20)
        if (shouldShowParticles) {
            val c = translations.getColor("prefix_login_color").color
            runFireWorks(p, Color.fromRGB(c.red, c.green, c.blue))
        }
        if(shouldPlaySound) {
            p.playSound(p.location, Sound.BLOCK_AMETHYST_BLOCK_STEP, 1F, 1F)
            authy.server.scheduler.scheduleSyncDelayedTask(authy, {
                p.playSound(p.location, Sound.BLOCK_AMETHYST_BLOCK_STEP, 1F, 1F)
            }, 10L)
            authy.server.scheduler.scheduleSyncDelayedTask(authy, {
                p.playSound(p.location, Sound.BLOCK_AMETHYST_BLOCK_STEP, 1F, 1F)
            }, 20L)
            authy.server.scheduler.scheduleSyncDelayedTask(authy, {
                p.playSound(p.location, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1F, 1F)
            }, 25L)
        }

    }

    fun runRegister(p : Player) {
        val shouldShowTitle = config.getBoolean("effects.title")
        val shouldShowParticles = config.getBoolean("effects.particles")
        val shouldPlaySound = config.getBoolean("effects.sounds")
        if(shouldShowTitle) p.sendTitle("${translations.getPrefix(PrefixType.REGISTER)} ${translations.get("register_success")}", "", 20, 20, 20)
        if (shouldShowParticles) {
            val c = translations.getColor("prefix_register_color").color
            runFireWorks(p, Color.fromRGB(c.red, c.green, c.blue))
        }
        if(shouldPlaySound) {
            p.playSound(p.location, Sound.BLOCK_TRIPWIRE_CLICK_ON, 1F, 1F)
            p.playSound(p.location, Sound.BLOCK_TRIPWIRE_CLICK_OFF, 1F, 1F)
            authy.server.scheduler.runTaskLater(authy, Runnable {
                p.playSound(p.location, Sound.BLOCK_TRIPWIRE_ATTACH, 1F, 1F)
            }, 5L)
        }
    }

    fun runUnregister(p : Player) {
        val shouldShowTitle = config.getBoolean("effects.title")
        val shouldShowParticles = config.getBoolean("effects.particles")
        val shouldPlaySound = config.getBoolean("effects.sounds")
        if(shouldShowTitle) p.sendTitle("${translations.getPrefix(PrefixType.UNREGISTER)} ${translations.get("unregister_success")}", "", 20, 20, 20)
        if (shouldShowParticles) {
            val c = translations.getColor("prefix_unregister_color").color
            runFireWorks(p, Color.fromRGB(c.red, c.green, c.blue))
        }
        if(shouldPlaySound) {
            p.playSound(p.location, Sound.BLOCK_CHAIN_BREAK, 1F, 1F)
            authy.server.scheduler.runTaskLater(authy, Runnable {
                p.playSound(p.location, Sound.BLOCK_CHAIN_BREAK, 1F, 1F)
            }, 5L)
            authy.server.scheduler.runTaskLater(authy, Runnable {
                p.playSound(p.location, Sound.BLOCK_CHAIN_PLACE, 1F, 1F)
            }, 5L)
        }
    }

    private fun runFireWorks(p : Player, c : Color) {
        authy.server.scheduler.runTaskLater(authy, Runnable {
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