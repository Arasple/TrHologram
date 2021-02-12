package me.arasple.mc.trhologram.module.action

import me.arasple.mc.trhologram.api.base.ClickHandler
import me.arasple.mc.trhologram.api.event.HologramInteractEvent.Type
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/12 14:11
 */
inline class ClickReaction(private val reactions: Map<Type, Reaction>) : ClickHandler {

    override fun eval(player: Player, type: Type) {
        reactions[Type.ALL]?.eval(player)
        reactions[type]?.eval(player)
    }

}