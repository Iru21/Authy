package me.iru.process

import me.iru.Authy
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import java.util.*

object PreLoginDataStore {

    private val authy = Authy.instance

    private val effects = mutableMapOf<UUID, Collection<PotionEffect>>()
    private val locations = mutableMapOf<UUID, Location>()
    private val fireTicks = mutableMapOf<UUID, Int>()
    private val flyingState = mutableMapOf<UUID, Boolean>()

    fun save(p: Player) {
        saveEffects(p)
        if(shouldConceal(p) && !p.isDead) {
            locations[p.uniqueId] = p.location
        }
        saveFireTicks(p)
        flyingState[p.uniqueId] = p.isFlying
    }

    fun restore(p: Player) {
        restoreEffects(p)
        if(shouldConceal(p)) {
            restoreLocation(p)
        }
        p.fireTicks = fireTicks[p.uniqueId] ?: 0
        fireTicks.remove(p.uniqueId)
        p.isFlying = flyingState[p.uniqueId] ?: false
        flyingState.remove(p.uniqueId)
    }

    private fun saveFireTicks(p: Player) {
        fireTicks[p.uniqueId] = p.fireTicks
        if(p.fireTicks > 0) {
            p.fireTicks = Int.MAX_VALUE
        }
    }

    private fun restoreLocation(p: Player) {
        val loc = locations[p.uniqueId]
        if(loc != null) {
            p.teleport(loc)
            locations.remove(p.uniqueId)
        }
    }

    private fun restoreEffects(p: Player) {
        val es = effects[p.uniqueId]

        if(es.isNullOrEmpty()) return

        for(e in es) {
            p.addPotionEffect(e)
        }

        effects.remove(p.uniqueId)
    }

    private fun saveEffects(p: Player) {
        effects[p.uniqueId] = p.activePotionEffects
        if (!effects[p.uniqueId].isNullOrEmpty()) {
            for (e in effects[p.uniqueId]!!) {
                p.removePotionEffect(e.type)
            }
        }
    }

    private fun shouldConceal(p: Player): Boolean {
        return authy.config.getBoolean("onJoin.teleport") &&
               authy.config.getBoolean("onJoin.concealment") &&
               p.hasPlayedBefore()
    }

}