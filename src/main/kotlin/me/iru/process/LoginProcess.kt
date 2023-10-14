package me.iru.process

import me.iru.Authy
import me.iru.PrefixType
import me.iru.utils.sendVersionDownload
import me.iru.utils.sendWelcomeMessage
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import java.util.*

class LoginProcess {
    val authy = Authy.instance
    val translations = Authy.translations
    val EffectRunner = EffectRunner()
    val playerData = Authy.playerData

    private val inProcess = HashSet<UUID>()
    private val tasksToCancel = HashMap<UUID, MutableList<BukkitTask>>()

    fun addPlayer(p: Player) {
        inProcess.add(p.uniqueId)
    }

    fun removePlayer(p: Player) {
        if(inProcess.contains(p.uniqueId)) {
            p.fallDistance = 0F
            p.removePotionEffect(PotionEffectType.BLINDNESS)
            PreLoginDataStore.restore(p)

            sendWelcomeMessage(p)
            sendVersionDownload(p)

            inProcess.remove(p.uniqueId)
        }
    }

    fun contains(e: Player): Boolean {
        return inProcess.contains(e.uniqueId)
    }

    fun sendPleaseAuthMessage(p: Player) {
        if(playerData.exists(p.uniqueId)) {
            p.sendMessage(
                "${translations.getPrefix(PrefixType.WARNING)} ${
                    translations.get("loginprocess_reminder_login").format(
                        if (playerData.get(p.uniqueId)!!.isPinEnabled) translations.get("loginprocess_reminder_pin") else ""
                    )
                }"
            )
        }
        else p.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("loginprocess_reminder_register").format(
            if (authy.config.getBoolean("requirePin")) translations.get("loginprocess_reminder_pin") else ""
        )}")
    }

    fun addTask(p: Player, task: BukkitTask) {
        if(!tasksToCancel.containsKey(p.uniqueId)) {
            tasksToCancel[p.uniqueId] = mutableListOf()
        }
        tasksToCancel[p.uniqueId]?.add(task)
    }

    fun cancelTasks(p: Player) {
        if(tasksToCancel.containsKey(p.uniqueId)) {
            tasksToCancel[p.uniqueId]?.forEach { it.cancel() }
            tasksToCancel.remove(p.uniqueId)
        }
    }
}