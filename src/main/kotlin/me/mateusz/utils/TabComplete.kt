package me.mateusz.utils

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter


class TabComplete : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        val completions : MutableList<String> = mutableListOf<String>()
        if(args.size <= 1) {
            if(command.name == "authy") completions.add("reload")
            else if(command.name == "pin") completions.addAll(mutableListOf("toggle", "set"))
        }
        return completions
    }
}