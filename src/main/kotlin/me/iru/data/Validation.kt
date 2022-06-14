package me.iru.data

import me.iru.Authy
import me.iru.utils.HashUtil
import java.util.*

object Validation {
    private val playerData = Authy.playerData

    fun checkPassword(uuid: UUID, password: String): Boolean {
        val data = playerData.get(uuid)!!
        return HashUtil.toSHA256(password) == data.hashedPassword
    }

    fun checkPin(uuid: UUID, pin: String): Boolean {
        val data = playerData.get(uuid)!!
        return HashUtil.toSHA256(pin) == data.hashedPin
    }

    fun passwordMatchesRules(pass: String): Boolean {
        return (pass.length >= 6 && pass.contains(Regex("[A-Z]")) && pass.contains(Regex("[0-9]")))
    }

    fun pinMatchesRules(pin: String): Boolean {
        return (pin.length == 6 && pin.contains(Regex("\\d{6}")))
    }
}