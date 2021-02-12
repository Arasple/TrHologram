package me.arasple.mc.trhologram.module.listener

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.module.display.Hologram
import me.arasple.mc.trhologram.util.Tasks
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

/**
 * @author Arasple
 * @date 2021/2/12 13:58
 */
@TListener
class ListenerRespawn : Listener {

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        Tasks.delay(2, true) {
            Hologram.refreshAll(e.player)
        }
    }

}