package me.mateusz.data

import me.mateusz.Authy
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.*

object Migration {
    val authy = Authy.instance
    val playerData = Authy.playerData

    fun updateSystem() {
        val preV4DataFolder = File(authy.dataFolder, "userdata" + File.separator)
        if(preV4DataFolder.exists()) {
            preV4DataFolder.renameTo(DataUtils.getDataFolder())
        }
    }

    fun updatePlayer(p: Player) {
        val dataFile = DataUtils.getDataFile(p.uniqueId)
        if(!dataFile.exists()) return
        val data = YamlConfiguration.loadConfiguration(dataFile)
        when(data.getInt("version")) {
            0 -> { // key not found in data file
                   val model = PlayerDataModel(
                       UUID.fromString(dataFile.name.dropLast(4)),
                       data.getString("usr")!!,
                       data.getString("ip")!!,
                       data.getString("pass")!!,
                       data.getBoolean("usePin"),
                       data.getString("pin"),
                       data.getLong("session")
                   )
                   playerData.save(model)
            }
        }
    }
}