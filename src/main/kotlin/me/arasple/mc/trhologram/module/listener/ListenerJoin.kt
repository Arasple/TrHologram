package me.arasple.mc.trhologram.module.listener

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

/**
 * @author Arasple
 * @date 2021/2/12 13:55
 */
@TListener
class ListenerJoin : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        Hologram.refreshAll(e.player)
    }

}