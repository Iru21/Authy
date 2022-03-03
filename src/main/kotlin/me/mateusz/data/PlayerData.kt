package me.mateusz.data

import me.mateusz.Authy
import me.mateusz.utils.HashUtil
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.lang.Exception
import java.util.*

class PlayerData {
    val authy = Authy.instance
    private val playerDataFolder = DataUtils.getDataFolder()

    fun init() {
        Migration.updateSystem()
        if(!playerDataFolder.exists()) playerDataFolder.mkdirs()
    }

    fun create(p: Player, password: String): PlayerDataModel {
        val dataFile = DataUtils.getDataFile(p.uniqueId)
        dataFile.createNewFile()
        val data = YamlConfiguration.loadConfiguration(dataFile)
        data.set("uuid", p.uniqueId)
        data.set("username", p.name)
        data.set("ip", p.address?.address?.hostAddress)
        data.set("hashedPassword", HashUtil.toSHA256(password))
        data.set("version", DataUtils.PLAYER_MODEL_VERSION)
        val model = DataUtils.construct(data)
        save(model)
        return model
    }

    fun delete(uuid: UUID): Boolean {
        try {
            val dataFile = DataUtils.getDataFile(uuid)
            if(!dataFile.exists()) return false
            dataFile.delete()
            return true
        } catch(e : Exception) {
            return false
        }
    }

    fun exists(p: Player): Boolean {
        return get(p.uniqueId) != null
    }

    fun get(uuid: UUID): PlayerDataModel? {
        val dataFile = DataUtils.getDataFile(uuid)
        if(!dataFile.exists()) return null
        return DataUtils.construct(YamlConfiguration.loadConfiguration(dataFile))
    }

    fun getAll(): List<PlayerDataModel> {
        val files = DataUtils.getDataFolder().listFiles()
        return files!!.map {
            DataUtils.construct(YamlConfiguration.loadConfiguration(it))
        }
    }

    fun save(playerDataModel: PlayerDataModel) {
        val dataFile = DataUtils.getDataFile(playerDataModel.uuid)
        if(!dataFile.exists()) dataFile.createNewFile()
        var data = YamlConfiguration.loadConfiguration(dataFile)
        data = playerDataModel.toYAML(data)
        data.save(dataFile)
    }

}