package me.iru.process

import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import java.util.*

object EffectStore {

    private val list = mutableMapOf<UUID, Collection<PotionEffect>>()

    fun save(p: Player) {
        list[p.uniqueId] = p.activePotionEffects
        if(!list[p.uniqueId].isNullOrEmpty()) {
            for(e in list[p.uniqueId]!!) {
                p.removePotionEffect(e.type)
            }
        }
    }

    fun restore(p: Player) {
        val es = list[p.uniqueId]

        if(es.isNullOrEmpty()) return

        for(e in es) {
            p.addPotionEffect(e)
        }
    }

}