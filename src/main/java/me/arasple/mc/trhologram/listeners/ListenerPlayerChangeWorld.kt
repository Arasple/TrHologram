package me.arasple.mc.trhologram.listeners

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.hologram.Hologram
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent

/**
 * @author Arasple
 * @date 2020/2/24 15:20
 */
@TListener
class ListenerPlayerChangeWorld : Listener {

    @EventHandler
    fun onChange(e: PlayerChangedWorldEvent) {
        Hologram.display(e.player)
    }

}