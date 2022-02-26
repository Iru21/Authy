package me.mateusz.process

import me.mateusz.Authy
import me.mateusz.PrefixType
import org.bukkit.entity.Player

object DuplicateProtection {
    fun check(p: Player): Boolean {
        val config = Authy.instance.config
        val translations = Authy.translations
        val protLevel = config.getInt("duplicateIpProtection.protectionLevel")
        val max = config.getInt("duplicateIpProtection.maxPerIp")
        val shouldNotify = config.getBoolean("duplicateIpProtection.notifyOnDuplicateIp")
        val duplicates = getDuplicatesForIpOf(p).filter {
            it != p.name
        }

        if(shouldNotify) {
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
        if(protLevel > 0 && duplicates.size + 1 > max) {
            p.kickPlayer(translations.get("duplicateprotection_max_reached").format(max.toString()))
            return false
        }
        return true
    }

    private fun getDuplicatesForIpOf(p: Player): MutableList<String> {
        val userData = UserData()
        val config = Authy.instance.config
        val protLevel = config.getInt("duplicateIpProtection.protectionLevel")
        val list = mutableListOf<String>()
        val ip = p.address?.address?.hostAddress
        if(protLevel == 1) {
            for (lp in Authy.instance.server.onlinePlayers) {
                val lpip = lp.address?.address?.hostAddress
                if(ip == lpip) {
                    list.add(lp.name)
                }
            }
        }
        else if(protLevel == 2) {
            for (lp in userData.getAll()) {
                val lpip = lp.get("ip")
                if(ip == lpip) {
                    list.add(lp.get("usr") as String)
                }
            }
        }
        return list
    }
}