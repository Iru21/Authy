package me.mateusz.interfaces

import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.java.JavaPlugin

interface ICommand : CommandExecutor {
    var name : String
}