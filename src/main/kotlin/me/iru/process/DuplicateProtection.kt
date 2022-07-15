package me.iru.process

import me.iru.Authy
import me.iru.PrefixType
import org.bukkit.entity.Player

object DuplicateProtection {

    val playerData = Authy.playerData
    val config = Authy.instance.config
    val translations = Authy.translations

    fun check(p: Player): Boolean {
        val protLevel = config.getInt("duplicateIpProtection.protectionLevel")
        if(protLevel == 0) return true
        val max = config.getInt("duplicateIpProtection.maxPerIp")
        val shouldNotify = config.getBoolean("duplicateIpProtection.notifyOnDuplicateIp")
        val duplicates = getDuplicatesForIpOf(p)

        if(shouldNotify && duplicates.size > 1) {
            for (lp in Authy.instance.server.onlinePlayers) {
                if(lp.hasPermission("authy.notifyonduplicateip")) {
                    val formatted =
                        duplicates.joinToString(translations.get("duplicateprotection_notification_separator")) {
                            "${translations.getColor("duplicateprotection_notification_namecolor")}$it"
                        }
                    lp.sendMessage("${translations.getPrefix(PrefixType.WARNING)} ${translations.get("duplicateprotection_notification_message").format(formatted)}")
                }
            }
        }
        return if(duplicates.size > max && !p.hasPermission("authy.ipbypass")) {
            p.kickPlayer(translations.get("duplicateprotection_max_reached").format(max.toString()))
            false
        }
        else true
    }

    private fun getDuplicatesForIpOf(p: Player): HashSet<String> {
        val protLevel = config.getInt("duplicateIpProtection.protectionLevel")
        val d = HashSet<String>()
        d.add(p.name)
        val ip = p.address?.address?.hostAddress
        when(protLevel) {
            1 -> {
                for (onlinePlayer in Authy.instance.server.onlinePlayers) {
                    val lpip = onlinePlayer.address?.address?.hostAddress
                    if(ip == lpip) {
                        d.add(onlinePlayer.name)
                    }
                }
            }
            2 -> {
                for (registeredPlayer in playerData.getAll()) {
                    val lpip = registeredPlayer.ip
                    if(ip == lpip) {
                        d.add(registeredPlayer.username)
                    }
                }
            }
        }
        return d
    }
}