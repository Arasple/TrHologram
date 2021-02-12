package me.arasple.mc.trhologram.api.hologram

import me.arasple.mc.trhologram.api.Position
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.api.base.ItemTexture
import me.arasple.mc.trhologram.api.nms.packet.*
import me.arasple.mc.trhologram.module.display.texture.Texture
import me.arasple.mc.trhologram.module.service.Performance
import me.arasple.mc.trhologram.util.Tasks
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/2/10 21:29
 *
 * Y: position.Y + 0.75
 */
class ItemHologram(
    texture: ItemTexture,
    position: Position,
    tick: Long = -1,
    onTick: (HologramComponent) -> Unit = {}
) : HologramComponent(position.clone(y = 1.3), tick, onTick) {

    constructor(
        itemStack: ItemStack,
        position: Position,
        tick: Long = -1,
        onTick: (HologramComponent) -> Unit = {}
    ) : this(
        Texture.createTexture(itemStack), position.clone(y = 1.3), tick, onTick
    )

    var display: ItemTexture = texture
        set(value) {
            forViewers { updateItem(it) }
            field = value
        }

    override fun spawn(player: Player) {
        destroy(player)
        val teid = TrHologramAPI.getIndex()

        PacketEntitySpawn(teid, position).send(player)
        PacketArmorStandModify(
            teid,
            isInvisible = true,
            isGlowing = false,
            isSmall = true,
            hasArms = false,
            noBasePlate = true,
            isMarker = true
        ).send(player)

        PacketEntitySpawn(entityId, position, false).send(player)
        updateItem(player)

        Tasks.delay {
            PacketEntityMount(teid, IntArray(1) { entityId }).send(player)
            Tasks.delay { PacketEntityDestroy(teid).send(player) }
        }

        viewers.add(player.name)
    }

    override fun onTick() {
        Performance.MIRROR.check("Hologram:onTick:ItemComponent") {
            forViewers { updateItem(it) }
        }
    }

    private fun updateItem(player: Player) {
        PacketItemModify(entityId, isInvisible = false, isGlowing = false, itemStack = display.generate(player)).send(
            player
        )
    }

}