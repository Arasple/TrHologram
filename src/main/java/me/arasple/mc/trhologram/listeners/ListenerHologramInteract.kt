package me.arasple.mc.trhologram.listeners

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.action.TrAction
import me.arasple.mc.trhologram.api.events.HologramInteractEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

/**
 * @author Arasple
 * @date 2020/2/14 9:12
 */
@TListener
class ListenerHologramInteract : Listener {

    @EventHandler
    fun onHologramInteract(e: HologramInteractEvent) {
        TrAction.runActions(e.hologram.actions, e.player)
    }

}
