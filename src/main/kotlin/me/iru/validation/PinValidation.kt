package me.iru.validation

import me.iru.Authy
import me.iru.utils.HashUtil
import java.util.*

object PinValidation {
    private val playerData = Authy.playerData

    fun check(uuid: UUID, pin: String): Boolean {
        val data = playerData.get(uuid)!!
        return HashUtil.toSHA256(pin) == data.hashedPin
    }

    fun matchesRules(pin: String): Boolean {
        val rule = getPinRule()
        if(pin.length < rule.minLength) return false
        if(pin.length > rule.maxLength) return false
        if(!pin.matches(Regex("^[0-9]*\$"))) return false
        return true
    }
}