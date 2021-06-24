package me.mateusz.utils
import java.security.MessageDigest

class HashUtil {
    private fun bytesToHex(hash: ByteArray): String {
        val hexString = StringBuilder(2 * hash.size)
        for (i in hash.indices) {
            val hex = Integer.toHexString(0xff and hash[i].toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }

    fun toSHA256(s : String) : String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashbytes = digest.digest(
            s.toByteArray()
        )
        return bytesToHex(hashbytes)
    }
}