package me.arasple.mc.trhologram.nms

import io.izzel.taboolib.module.packet.Packet
import io.izzel.taboolib.module.packet.TPacket
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.api.TrHologramAPI.getHologramByEntityId
import me.arasple.mc.trhologram.api.events.HologramInteractEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/1/29 22:16
 */
object HoloInteract {

    @TPacket(type = TPacket.Type.RECEIVE)
    fun interactEntity(player: Player, packet: Packet): Boolean {
        try {
            if (!player.isOnline) {
                return true
            }
            if (packet.`is`("PacketPlayInUseEntity")) {
                val id = packet.read("a") as Int
                val hologram = getHologramByEntityId(id)
                if (hologram != null) {
                    Bukkit.getScheduler().runTask(TrHologram.getPlugin(), Runnable { HologramInteractEvent(player, hologram).call() })
                    return true
                }
            }
        } catch (e: Throwable) {
            TrHologram.LOGGER.error("An error occurred while processing hologram interact event. See the exception for details.")
            e.printStackTrace()
        }
        return true
    }

}
