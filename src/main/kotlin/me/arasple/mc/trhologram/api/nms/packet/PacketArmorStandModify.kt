package me.arasple.mc.trhologram.api.nms.packet

/**
 * @author Arasple
 * @date 2021/1/25 12:20
 */
class PacketArmorStandModify(
    entityId: Int,
    val isInvisible: Boolean,
    val isGlowing: Boolean,
    val isSmall: Boolean,
    val hasArms: Boolean,
    val noBasePlate: Boolean,
    val isMarker: Boolean
) : PacketEntity(entityId)