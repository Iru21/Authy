package me.mateusz.events

import me.mateusz.process.LoginProcess
import me.mateusz.process.Session
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.*
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent

import org.bukkit.event.entity.EntityDamageEvent.DamageCause

import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.TimeUnit


class LoginEvents(jplugin : JavaPlugin, preLoginProcess: LoginProcess) : Listener {

    val LoginProcess : LoginProcess = preLoginProcess

    val plugin = jplugin

    val Session = Session(plugin)

    @EventHandler
    fun onJoin(e : PlayerJoinEvent) {

        lateinit var task0 : BukkitTask
        var loc = e.player.location
        task0 = plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            if(loc.block.getRelative(BlockFace.DOWN).type.isAir) {
                loc = Location(loc.world, loc.x, loc.y - 1, loc.z)
            } else {
                task0.cancel()
                e.player.teleport(loc)
            }
        }, 0L, 0L)

        if(Session.tryAutoLogin(e.player)) {
            e.player.sendMessage("${ChatColor.of("#afffb1")}§l(✔) §7Automatycznie zalogowano!")
            if(plugin.config.getBoolean("SendWelcomeMessage")) {
                for(message : String in plugin.config.getStringList("WelcomeMessage")) {
                    e.player.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
                }
            }
            return
        }

        LoginProcess.addPlayer(e.player)
        var i = 0
        lateinit var task : BukkitTask
        task = plugin.server.scheduler.runTaskTimer(plugin, Runnable {
            if(LoginProcess.checkIfContains(e.player)) {
                if(i == 240) {
                    task.cancel()
                    e.player.kickPlayer("§c§l(!) §7Minal czas na autoryzacje!")
                    LoginProcess.removePlayer(e.player)
                }
                LoginProcess.sendPleaseAuthMessage(e.player)
                i++
            }
            else task.cancel()
        },0L, 200L)
    }

    @EventHandler
    fun onQuit(e : PlayerQuitEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            LoginProcess.removePlayer(e.player)
        }
    }

    @EventHandler
    fun onMove(e : PlayerMoveEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockBreak(e : BlockBreakEvent) {
        if (LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlace(e : BlockPlaceEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onItemConsume(e : PlayerItemConsumeEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onItemDrop(e : PlayerDropItemEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onUse(e : PlayerInteractEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityUse(e : PlayerInteractAtEntityEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityUse2(e : PlayerInteractEntityEvent) {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onDamageTaken(e : EntityDamageEvent) {
        if(e.entity is Player) {
            if (LoginProcess.checkIfContains((e.entity as Player).player as Player)) {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onAttack(e : EntityDamageByEntityEvent) {
        if(e.damager is Player) {
            if (LoginProcess.checkIfContains(e.damager as Player)) {
                e.isCancelled = true
            }
        }
    }


    @EventHandler
    fun onAnyCommand(e : PlayerCommandPreprocessEvent) {
        val allowedCommands = mutableListOf<String>("/l", "/login", "/reg", "/register")
        if(LoginProcess.checkIfContains(e.player)) {
            if (!allowedCommands.contains(e.message.split(" ")[0])) {
                e.isCancelled = true
                LoginProcess.sendPleaseAuthMessage(e.player)
            }
        }
    }

    @EventHandler
    fun onChat(e : AsyncPlayerChatEvent)  {
        if(LoginProcess.checkIfContains(e.player)) {
            e.isCancelled = true
            LoginProcess.sendPleaseAuthMessage(e.player)
        }
    }
}