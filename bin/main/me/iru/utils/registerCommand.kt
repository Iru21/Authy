package me.iru.utils

import me.iru.Authy
import me.iru.interfaces.ICommand

fun registerCommand(c : ICommand) {
    with(Authy.instance) {
        getCommand(c.name)?.setExecutor(c)
        getCommand(c.name)?.setTabCompleter(TabComplete())
    }
}