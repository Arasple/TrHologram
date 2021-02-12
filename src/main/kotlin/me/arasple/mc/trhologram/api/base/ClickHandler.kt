package me.arasple.mc.trhologram.api.base

import me.arasple.mc.trhologram.api.event.HologramInteractEvent
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/12 14:10
 */
fun interface ClickHandler {

    fun eval(player: Player, type: HologramInteractEvent.Type)

}