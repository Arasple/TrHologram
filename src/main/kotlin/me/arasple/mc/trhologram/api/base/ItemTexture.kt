package me.arasple.mc.trhologram.api.base

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/2/11 22:50
 */
fun interface ItemTexture {

    fun generate(player: Player): ItemStack

}