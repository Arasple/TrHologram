package me.arasple.mc.trhologram.module.listener

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.api.nms.NMSListener
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author Arasple
 * @date 2021/2/12 13:55
 */
@TListener
class ListenerQuit : Listener {

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        val player = e.player

        Hologram.destroyAll(player)
    }

}