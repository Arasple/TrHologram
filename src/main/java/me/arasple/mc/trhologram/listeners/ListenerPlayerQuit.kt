package me.arasple.mc.trhologram.listeners

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.hologram.Hologram
import me.arasple.mc.trhologram.hologram.HologramManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.function.Consumer

/**
 * @author Arasple
 * @date 2020/2/14 9:16
 */
@TListener
class ListenerPlayerQuit : Listener {

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        HologramManager.getHolograms().forEach(Consumer { hologram: Hologram -> hologram.viewers.remove(e.player.uniqueId) })
    }

}
