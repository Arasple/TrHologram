package me.arasple.mc.trhologram.api.hologram

import me.arasple.mc.trhologram.api.Position
import me.arasple.mc.trhologram.api.nms.packet.PacketArmorStandModify
import me.arasple.mc.trhologram.api.nms.packet.PacketArmorStandName
import me.arasple.mc.trhologram.api.nms.packet.PacketEntitySpawn
import me.arasple.mc.trhologram.module.service.Performance
import me.arasple.mc.trhologram.util.Parser
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/10 10:28
 *
 * Y: rawY + 1 ~ rawY + 1.25
 */
class TextHologram(
    name: String,
    position: Position,
    tick: Long,
    onTick: (HologramComponent) -> Unit = {}
) : HologramComponent(position, tick, onTick) {

    var text: String = name
        set(value) {
            onTick()
            field = value
        }

    private fun updateName(player: Player) {
        PacketArmorStandName(entityId, true, Parser.parseString(player, text)).send(player)
    }

    override fun spawn(player: Player) {
        PacketEntitySpawn(entityId, position).send(player)
        PacketArmorStandModify(
            entityId,
            isInvisible = true,
            isGlowing = false,
            isSmall = true,
            hasArms = false,
            noBasePlate = true,
            isMarker = false
        ).send(player)
        updateName(player)

        viewers.add(player.name)
    }

    override fun onTick() {
        Performance.MIRROR.check("Hologram:onTick:TextComponent") {
            forViewers { updateName(it) }
        }
    }

}