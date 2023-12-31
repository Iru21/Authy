package me.iru.utils

fun hasValidName(name: String): Boolean {
    return name.matches(Regex("^\\w{3,16}$"))
}