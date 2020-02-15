package me.arasple.mc.trhologram.listeners

import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.Strings
import me.arasple.mc.trhologram.action.TrAction
import me.arasple.mc.trhologram.api.events.HologramInteractEvent
import me.arasple.mc.trhologram.utils.JavaScript
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
        val actionGroups = e.hologram.actions
        if (actionGroups != null && actionGroups.isNotEmpty()) {
            actionGroups.sortBy { a -> a.priority }
            for (action in actionGroups.reversed()) {
                if (Strings.isEmpty(action.requirement) || JavaScript.run(e.player, action.requirement) as Boolean) {
                    TrAction.runActions(action.actions, e.player)
                    break
                }
            }
        }
    }

}
