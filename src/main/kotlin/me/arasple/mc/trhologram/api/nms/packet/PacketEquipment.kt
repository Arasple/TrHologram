package me.arasple.mc.trhologram.api.nms.packet

import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/12/4 21:22
 */
class PacketEquipment(entityId: Int, val slot: EquipmentSlot, val itemStack: ItemStack) : PacketEntity(entityId)