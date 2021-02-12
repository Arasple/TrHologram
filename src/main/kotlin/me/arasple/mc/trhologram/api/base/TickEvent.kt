package me.arasple.mc.trhologram.api.base

import me.arasple.mc.trhologram.api.hologram.HologramComponent

/**
 * @author Arasple
 * @date 2021/2/12 20:38
 */
fun interface TickEvent {

    fun run(hologramComponent: HologramComponent)

}