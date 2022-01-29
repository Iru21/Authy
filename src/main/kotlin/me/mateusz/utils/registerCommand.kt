package me.mateusz.utils

import me.mateusz.Authy
import me.mateusz.interfaces.ICommand

fun registerCommand(c : ICommand) {
    with(Authy.instance) {
        getCommand(c.name)?.setExecutor(c)
        getCommand(c.name)?.setTabCompleter(TabComplete())
    }
}