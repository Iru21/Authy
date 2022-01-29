package me.mateusz.interfaces

import org.bukkit.command.CommandExecutor

interface ICommand : CommandExecutor {
    var name: String
}