package me.arasple.mc.trhologram.module.listener

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent

/**
 * @author Arasple
 * @date 2021/2/12 13:58
 */
@TListener
class ListenerWorldChange : Listener {

    @EventHandler
    fun onChange(e: PlayerChangedWorldEvent) {
        Hologram.destroyAll(e.player)
        Hologram.refreshAll(e.player)
    }

}