package me.iru.utils

import me.iru.Authy
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun sendVersionDownload(sender: CommandSender) {
    val authy = Authy.instance

    if(authy.latestVersion != authy.version) {
        sender.sendMessage("§8[§6Authy§8] §7New version of §6Authy§7 is available§8: §c${authy.latestVersion}§8!")

        if (sender is Player && sender.isOp) {
            val clickable = fun(text: String, url: String): TextComponent {
                return TextComponent(text).apply {
                    clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
                    hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("Click to open this download page!"))
                }
            }

            sender.spigot().sendMessage(
                TextComponent("§8[§6Authy§8] §7Download: "),
                clickable("§aModrinth", "https://modrinth.com/plugin/authy/version/latest"),
                TextComponent("§7, "),
                clickable("§6Spigot", "https://www.spigotmc.org/resources/authy.100004/"),
                TextComponent("§7, "),
                clickable("§dGitHub", "https://github.com/Iru21/Authy/releases/latest"),
                TextComponent("§7.")
            )
        }
    }
}