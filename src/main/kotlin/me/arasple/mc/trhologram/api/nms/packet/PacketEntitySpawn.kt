package me.arasple.mc.trhologram.api.nms.packet

import me.arasple.mc.trhologram.api.Position

/**
 * @author Arasple
 * @date 2021/1/25 12:20
 */
class PacketEntitySpawn(entityId: Int, val position: Position, val type: Boolean = true) : PacketEntity(entityId)