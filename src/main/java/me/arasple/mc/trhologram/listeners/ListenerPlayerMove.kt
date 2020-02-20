package me.arasple.mc.trhologram.listeners

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.hologram.Hologram
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import java.util.function.Consumer

/**
 * @author Arasple
 * @date 2020/2/14 9:13
 */
@TListener
class ListenerPlayerMove : Listener {

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        val player = e.player
        Hologram.getHolograms().forEach(Consumer { hologram ->
            if (hologram.isVisible(player) && !hologram.viewers.contains(player)) {
                hologram.display(player)
            } else if (!hologram.isVisible(player) && hologram.viewers.contains(player)) {
                hologram.destroy(player)
            }
        })
    }

}