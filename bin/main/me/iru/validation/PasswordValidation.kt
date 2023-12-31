package me.iru.validation

import me.iru.Authy
import me.iru.utils.HashUtil
import java.util.*

object PasswordValidation {
    private val playerData = Authy.playerData

    fun check(uuid: UUID, password: String): Boolean {
        val data = playerData.get(uuid)!!
        return HashUtil.toSHA256(password) == data.password
    }

    fun matchesRules(pass: String): Boolean {
        val rule = getPasswordRule()
        if(pass.length < rule.minLength) return false
        if(pass.length > rule.maxLength) return false
        if(!pass.contains(Regex("[A-Z]{${rule.minUppercase}}"))) return false
        if(!pass.contains(Regex("[0-9]{${rule.minNumbers}}"))) return false
        return true
    }
}