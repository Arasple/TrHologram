package me.arasple.mc.trhologram.api.event

import io.izzel.taboolib.module.event.EventCancellable
import io.izzel.taboolib.module.event.EventNormal
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/11 16:34
 */
class HologramInteractEvent(val player: Player, val type: Type, val hologram: Hologram) : EventCancellable<HologramInteractEvent>() {

    init {
        async(!Bukkit.isPrimaryThread())
    }

    enum class Type {

        ALL,

        LEFT,

        RIGHT,

        SHIFT_LEFT,

        SHIFT_RIGHT

    }

}