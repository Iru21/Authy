package me.iru.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.iru.Authy
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


fun isNewVersionAvailable(): Pair<Boolean, String> {
    val current = Authy.instance.version
    val latestVersion = getLatestRelease().get("tag_name")?.asString ?: current
    return Pair(versionToNum(current) < versionToNum(latestVersion), latestVersion)
}

private fun versionToNum(ver: String): Int {
    val v = ver.removePrefix("v")
    var a = 0
    val b = v.split(".")
    a += b[0].toInt() * 100
    a += b[1].toInt() * 10
    a += b[2].toInt()
    return a
}

private fun getLatestRelease(): JsonObject {
    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder(
        URI.create("https://api.github.com/repos/Iru21/Authy/releases/latest"))
        .header("accept", "application/json")
        .build()

    val res = client.send(request, HttpResponse.BodyHandlers.ofString())
    return JsonParser().parse(res.body()).asJsonObject
}