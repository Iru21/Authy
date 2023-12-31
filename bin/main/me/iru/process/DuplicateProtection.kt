package me.iru.process

import me.iru.Authy
import me.iru.PrefixType
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerLoginEvent

object DuplicateProtection {

    val playerData = Authy.playerData
    val translations = Authy.translations
    val authy = Authy.instance
    val config = authy.config

    fun check(e: PlayerLoginEvent) {
        val p = e.player
        if(authy.server.getPlayer(p.name) != null) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "")
        }

        val level = config.getInt("duplicateIpProtection.protectionLevel")
        if(level == 0) return
        val max = config.getInt("duplicateIpProtection.maxPerIp")
        val shouldNotify = config.getBoolean("duplicateIpProtection.notifyOnDuplicateIp")
        val duplicates = getDuplicatesForIpOf(p)

        if(shouldNotify && duplicates.size > 1) {
            for (player in Authy.instance.server.onlinePlayers) {
                if(player.hasPermission("authy.notifyonduplicateip")) {
                    val formatted =
                        duplicates.joinToString(translations.get("duplicateprotection_notification_separator")) {
                            "${translations.getColor("duplicateprotection_notification_namecolor")}$it"
                        }
                    player.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("duplicateprotection_notification_message").format(formatted)}")
                }
            }
        }
        if(duplicates.size > max && !p.hasPermission("authy.ipbypass")) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, translations.get("duplicateprotection_max_reached").format(max.toString()))
        }
    }

    private fun getDuplicatesForIpOf(p: Player): HashSet<String> {
        val level = config.getInt("duplicateIpProtection.protectionLevel")
        val duplicates = HashSet<String>()
        duplicates.add(p.name)
        val ip = p.address?.address?.hostAddress
        return when(level) {
            1 -> {
                Authy.instance.server.onlinePlayers.filter { it.address?.address?.hostAddress == ip }.mapTo(duplicates) { it.name }
            }
            2 -> {
                playerData.getAll().filter { it.ip == ip }.mapTo(duplicates) { it.username }
            }
            else -> {
                duplicates
            }
        }
    }
}