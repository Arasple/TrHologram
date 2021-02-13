package me.arasple.mc.trhologram.api.nms

import io.izzel.taboolib.module.packet.Packet
import io.izzel.taboolib.module.packet.TPacket
import me.arasple.mc.trhologram.api.event.HologramInteractEvent
import me.arasple.mc.trhologram.api.event.HologramInteractEvent.Type.*
import me.arasple.mc.trhologram.module.display.Hologram
import me.arasple.mc.trhologram.module.service.Performance
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/10 11:27
 */
object NMSListener {

    @TPacket(type = TPacket.Type.RECEIVE)
    fun useEntity(player: Player, packet: Packet): Boolean {
        if (packet.`is`("PacketPlayInUseEntity")) {
            Performance.MIRROR.check("Hologram:Event:Interact") {
                val entityId = packet.read("a", -1).also { if (it < 1197897763) return true }
                val hologram =
                    Hologram.findHologram { it -> it.components.any { it.entityId == entityId } } ?: return true

                val sneaking = player.isSneaking
                val type = when (packet.read("action").toString()) {
                    "ATTACK" -> if (sneaking) SHIFT_LEFT else LEFT
                    else -> if (sneaking) SHIFT_RIGHT else RIGHT
                }

                HologramInteractEvent(player, type, hologram).call()
            }
        }
        return true
    }

}