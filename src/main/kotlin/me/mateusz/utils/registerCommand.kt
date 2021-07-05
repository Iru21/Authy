package me.mateusz.utils

import me.mateusz.interfaces.ICommand
import org.bukkit.plugin.java.JavaPlugin

fun registerCommand(t : JavaPlugin, c : ICommand) {
    with(t) {
        getCommand(c.name)?.setExecutor(c)
        getCommand(c.name)?.setTabCompleter(TabComplete())
    }
}