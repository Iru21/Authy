package me.iru.interfaces

import org.bukkit.command.CommandExecutor

interface ICommand : CommandExecutor {
    var name: String
}