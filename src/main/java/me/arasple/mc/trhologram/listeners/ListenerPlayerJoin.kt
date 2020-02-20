package me.arasple.mc.trhologram.listeners

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.hologram.Hologram
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author Arasple
 * @date 2020/2/14 9:13
 */
@TListener
class ListenerPlayerJoin : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        Hologram.getHolograms().stream().filter { hologram -> hologram.isVisible(e.player) }.forEach { hologram -> hologram.display(e.player) }
    }

}
