package me.arasple.mc.trhologram.api.nms.packet

import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/2/10 10:19
 */
class PacketItemModify(
    entityId: Int,
    val isInvisible: Boolean,
    val isGlowing: Boolean,
    val itemStack: ItemStack
) : PacketEntity(entityId)