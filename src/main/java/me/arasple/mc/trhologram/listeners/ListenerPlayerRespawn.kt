package me.arasple.mc.trhologram.listeners

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.hologram.Hologram
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

/**
 * @author Arasple
 * @date 2020/2/28 15:24
 */
@TListener
class ListenerPlayerRespawn : Listener {

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        Bukkit.getScheduler().runTaskLater(TrHologram.getPlugin(), Runnable {
            Hologram.display(e.player)
        }, 5)
    }

}