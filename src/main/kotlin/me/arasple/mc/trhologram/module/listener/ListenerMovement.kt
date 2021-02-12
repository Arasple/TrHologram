package me.arasple.mc.trhologram.module.listener

import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.lite.cooldown.Cooldown
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

/**
 * @author Arasple
 * @date 2021/2/11 9:58
 */
@TListener
class ListenerMovement : Listener {

    val cd = Cooldown("TrHologram:MoveCheck", 1)

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        val player = e.player

        if (!cd.isCooldown(player.name)) {
            Hologram.refreshAll(player)
        }
    }

}