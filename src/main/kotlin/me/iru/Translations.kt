package me.iru

import net.md_5.bungee.api.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.regex.Pattern

enum class PrefixType {
    WARNING,
    ERROR,
    LOGIN,
    PIN,
    REGISTER,
    REMEMBER,
    UNREGISTER
}

enum class ParseMode {
    ResetAndTranslate,
    Translate,
    None
}

class Translations {
    private val authy = Authy.instance
    private val defaultLangFolder = File(authy.dataFolder, "lang" + File.separator + "defaults" + File.separator)
    private val defaultLangs = arrayListOf("en_us", "cs_cz", "es_es", "pl_pl", "ru_ru", "tr_tr", "zh_tw", "zh_cn")
    private var cache: YamlConfiguration? = null

    companion object {
        const val TRANSLATION_VERSION = 8
    }

    init {
        checkDefaults()
        updateCache()
    }

    fun getPrefix(type: PrefixType): String {
        return "${getColor("prefix_${type.name.lowercase()}_color")}${get("prefix_${type.name.lowercase()}_value",
            ParseMode.Translate
        )}"
    }

    fun getColor(key: String): ChatColor {
        val mcRegex = Pattern.compile("^&([0-9a-f])\$")
        val hexRegex = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\$")
        val value = get(key, ParseMode.None)
        return if(hexRegex.matcher(value).matches()) {
            ChatColor.of(value)
        } else if(mcRegex.matcher(value).matches()) {
            ChatColor.getByChar(value[1])
        } else {
            ChatColor.BLACK
        }
    }

    private fun getFallback(key: String): String {
        authy.server.consoleSender.sendMessage("${authy.prefix} §cThere has been a translation error! Couldn't find key §6$key §cin §6${authy.config.getString("lang")}")
        val en = File(authy.dataFolder, "lang" + File.separator + "en_us.yml")
        var fallback = YamlConfiguration.loadConfiguration(en).getString(key)
        if(fallback == null) {
            authy.server.consoleSender.sendMessage("${authy.prefix} §cFallback key §6$key §cnot found in lang file §6en_us§c! Please update file §6plugins/Authy/lang/en_us.yml")
            fallback = "§cTranslation error, please contact an administrator!"
        }
        return fallback
    }

    fun get(key: String, mode: ParseMode = ParseMode.ResetAndTranslate): String {
        val value = cache?.getString(key) ?: getFallback(key)
        return when(mode) {
            ParseMode.ResetAndTranslate -> ChatColor.translateAlternateColorCodes('&', "&r$value")
            ParseMode.Translate -> ChatColor.translateAlternateColorCodes('&', value)
            ParseMode.None -> value
        }
    }

    fun updateCache() {
        val selectedLang = authy.config.getString("lang")
        var langFile = File(authy.dataFolder, "lang" + File.separator + selectedLang + ".yml")
        if(!langFile.exists()) {
            authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}${authy.description.name}${org.bukkit.ChatColor.DARK_GRAY}] ${ChatColor.RED}No language file for $selectedLang found! Saving default...")
            checkDefaults()
        }
        if(hasOldVersion(selectedLang!!)) {
            langFile = File(authy.dataFolder, "lang" + File.separator + "defaults" + File.separator + "en_us.yml")
            authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}${authy.description.name}${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.AQUA} Selected language has been reverted to default en_us, because $selectedLang is not on the newest version ($TRANSLATION_VERSION)")
        }
        cache = YamlConfiguration.loadConfiguration(langFile)
    }

    private fun checkDefaults() {
        if(!defaultLangFolder.exists()) {
            defaultLangFolder.mkdirs()
        }
        for (lang in defaultLangs) {
            val langFile = File(authy.dataFolder, "lang" + File.separator + lang + ".yml")
            if(!langFile.exists()) {
                authy.saveResource("lang${File.separator}$lang.yml", false)
            }
            authy.saveResource("lang${File.separator}defaults${File.separator}$lang.yml", true)
        }
    }

    private fun hasOldVersion(l: String): Boolean {
        val langFile = File(authy.dataFolder, "lang" + File.separator + l + ".yml")
        val parsed = YamlConfiguration.loadConfiguration(langFile)
        return TRANSLATION_VERSION > parsed.getInt("version")
    }

}
