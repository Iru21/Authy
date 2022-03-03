package me.mateusz.data

import me.mateusz.Authy
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

object DataUtils {
    val authy = Authy.instance
    const val PLAYER_MODEL_VERSION = 4

    fun getDataFolder(): File {
        return File(authy.dataFolder, "player-data" + File.separator)
    }

    fun getDataFile(uuid: UUID): File {
        return File(getDataFolder(), "$uuid.yml")
    }

    fun construct(data: YamlConfiguration): PlayerDataModel {
        return PlayerDataModel(
            UUID.fromString(data.getString("uuid")!!),
            data.getString("username")!!,
            data.getString("ip")!!,
            data.getString("hashedPassword")!!,
            data.getBoolean("usePin"),
            data.getString("hashedPin"),
            data.getLong("session"),
            data.getInt("version")
        )
    }

}