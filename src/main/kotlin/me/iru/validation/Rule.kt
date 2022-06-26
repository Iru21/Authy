package me.iru.validation

import me.iru.Authy

class PinRule(var minLength: Int, var maxLength: Int) {
    init {
        if(minLength < 1) {
            minLength = 1
        }
        if(maxLength > 10) {
            maxLength = 10
        }
        if (minLength > maxLength) {
            minLength = maxLength
        }
    }
}

class PasswordRule(var minLength: Int, var maxLength: Int, var minUppercase: Int, var minNumbers: Int) {
    val authy = Authy.instance
    init {
        if(minLength <= 1) {
            minLength = 1
        }
        if(maxLength > 32) {
            maxLength = 32
        }
        if (minLength > maxLength) {
            minLength = 31
        }
        if(minUppercase > maxLength) {
            minUppercase = maxLength
        }
        if(minNumbers > maxLength) {
            minNumbers = maxLength
        }
        if(minUppercase + minNumbers > maxLength) {
            minUppercase = 0
            minNumbers = 0
            authy.server.consoleSender.sendMessage("${org.bukkit.ChatColor.DARK_GRAY}[${org.bukkit.ChatColor.GOLD}${authy.description.name}${org.bukkit.ChatColor.DARK_GRAY}] ${org.bukkit.ChatColor.RED} minUppercase and minNumbers can't be bigger than maxLength! Values of minUppercase and minNumbers had been set to 0")
        }
    }
}

fun getPasswordRule(): PasswordRule {
    val config = Authy.instance.config
    return PasswordRule(
        config.getInt("passwordValidation.minLength"),
        config.getInt("passwordValidation.maxLength"),
        config.getInt("passwordValidation.minUppercase"),
        config.getInt("passwordValidation.minNumbers")
    )
}

fun getPinRule(): PinRule {
    val config = Authy.instance.config
    return PinRule(
        config.getInt("pinValidation.minLength"),
        config.getInt("pinValidation.maxLength"),
    )
}