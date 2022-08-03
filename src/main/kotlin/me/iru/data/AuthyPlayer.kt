package me.iru.data

import java.util.*

data class AuthyPlayer(val uuid: UUID, var username: String, var ip: String, var password: String, var isPinEnabled: Boolean = false, var pin: String? = null, var session: Long = 0L)