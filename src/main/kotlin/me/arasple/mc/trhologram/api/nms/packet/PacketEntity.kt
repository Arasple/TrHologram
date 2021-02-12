package me.arasple.mc.trhologram.api.nms.packet

import me.arasple.mc.trhologram.api.nms.NMS
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Arasple
 * @date 2020/12/4 21:22
 */
abstract class PacketEntity(val entityId: Int = -1, val uuid: UUID? = null) {

    fun send(player: Player) = NMS.INSTANCE.sendEntityPacket(player, this)

}