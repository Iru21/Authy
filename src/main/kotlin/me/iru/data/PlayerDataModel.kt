package me.iru.data

import org.bukkit.configuration.file.YamlConfiguration
import java.util.*

class PlayerDataModel(var uuid: UUID, var username: String, var ip: String, var hashedPassword: String, var usePin: Boolean = false, var hashedPin: String? = null, var session: Long = 0L, var version: Int = DataUtils.PLAYER_MODEL_VERSION) {
    fun toYAML(data: YamlConfiguration): YamlConfiguration {
        for(field in javaClass.declaredFields) {
            var value = field.get(this)
            if(value is UUID) {
                value = value.toString()
            }
            data.set(field.name, value)
        }
        return data
    }
}