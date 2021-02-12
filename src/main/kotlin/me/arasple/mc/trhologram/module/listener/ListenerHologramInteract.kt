package me.arasple.mc.trhologram.module.listener

import io.izzel.taboolib.module.inject.TListener
import me.arasple.mc.trhologram.api.Settings
import me.arasple.mc.trhologram.api.event.HologramInteractEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

/**
 * @author Arasple
 * @date 2021/2/11 16:41
 */
@TListener
class ListenerHologramInteract : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onInteract(e: HologramInteractEvent) {
        val player = e.player

        if (!Settings.INSTANCE.interactDelay.isCooldown(player.name)) {
            e.hologram.reactions.eval(player, e.type)
        }
    }

}