package me.arasple.mc.trhologram.listeners

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.hologram.Hologram
import me.arasple.mc.trhologram.hologram.HologramManager
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
        HologramManager.getHolograms().stream().filter { hologram: Hologram -> hologram.isVisible(e.player) }.forEach { hologram: Hologram -> hologram.display(e.player) }
    }

}
