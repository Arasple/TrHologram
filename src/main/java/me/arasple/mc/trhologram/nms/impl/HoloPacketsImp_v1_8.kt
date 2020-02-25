package me.arasple.mc.trhologram.nms.impl

import io.izzel.taboolib.module.lite.SimpleReflection
import io.izzel.taboolib.module.packet.TPacketHandler
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.nms.HoloPackets
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*


/**
 * @author Arasple
 * @date 2020/2/24 23:07
 */
class HoloPacketsImp_v1_8 : HoloPackets() {

    companion object {
        init {
            SimpleReflection.checkAndSave(
                    PacketPlayOutSpawnEntity::class.java,
                    PacketPlayOutEntityTeleport::class.java,
                    PacketPlayOutEntityMetadata::class.java
            )
        }
    }

    override fun spawnArmorStand(player: Player, entityId: Int, uuid: UUID, location: Location) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutSpawnEntity::class.java, PacketPlayOutSpawnEntity(),
                mapOf(
                        Pair("a", entityId),
                        Pair("b", MathHelper.floor(location.x * 32.0)),
                        Pair("c", MathHelper.floor(location.y * 32.0)),
                        Pair("d", MathHelper.floor(location.z * 32.0)),
                        Pair("j", 78)
                )
        ))
        initArmorStandAsHologram(player, entityId)
    }

    override fun spawnItem(player: Player, entityId: Int, uuid: UUID, location: Location, itemStack: ItemStack) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutSpawnEntity::class.java, PacketPlayOutSpawnEntity(),
                mapOf(
                        Pair("a", entityId),
                        Pair("b", MathHelper.floor(location.x * 32.0)),
                        Pair("c", MathHelper.floor(location.y * 32.0)),
                        Pair("d", MathHelper.floor(location.z * 32.0)),
                        Pair("j", 2)
                )
        ))
        sendEntityMetadata(player, entityId, getMetaEntityItemStack(itemStack))
    }

    override fun destroyEntity(player: Player, entityId: Int) {
        TPacketHandler.sendPacket(player, PacketPlayOutEntityDestroy(entityId))
    }

    override fun initArmorStandAsHologram(player: Player, entityId: Int) {
        sendEntityMetadata(player, entityId,
                getMetaEntityCustomNameVisible(true),
                getMetaEntitySilenced(true),
                getMetaEntityProperties(false, false, true, false, true, false, false),
                getMetaArmorStandProperties(TrHologram.SETTINGS.getBoolean("ARMORSTAND-SMALL", true), false, true, false)
        )
    }

    override fun updateArmorStandDisplayName(player: Player, entityId: Int, name: String) {
        sendEntityMetadata(player, entityId, getMetaEntityCustomName(name))
    }

    override fun updateArmorStandLocation(player: Player, entityId: Int, location: Location) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutEntityTeleport::class.java, PacketPlayOutEntityTeleport(),
                mapOf(
                        Pair("a", entityId),
                        Pair("b", MathHelper.floor(location.x)),
                        Pair("c", MathHelper.floor(location.y)),
                        Pair("d", MathHelper.floor(location.z)),
                        Pair("e", 0),
                        Pair("f", 0),
                        Pair("g", false)
                )
        ))
    }

    override fun updatePassengers(player: Player, entityId: Int, vararg passengers: Int) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutAttachEntity::class.java, PacketPlayOutAttachEntity(),
                mapOf(
                        Pair("a", entityId),
                        Pair("b", passengers[0])
                )
        ))
    }

    override fun updateArmorStandEquipmentItem(player: Player, entityId: Int, slot: EquipmentSlot, itemStack: ItemStack) {
        TPacketHandler.sendPacket(player, PacketPlayOutEntityEquipment(entityId, 4, CraftItemStack.asNMSCopy(itemStack)))
    }

    override fun sendEntityMetadata(player: Player, entityId: Int, vararg objects: Any) {
        val items: MutableList<DataWatcher.WatchableObject> = ArrayList()
        for (obj in objects) {
            items.add(obj as DataWatcher.WatchableObject)
        }
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutEntityMetadata::class.java, PacketPlayOutEntityMetadata(),
                mapOf(
                        Pair("a", entityId),
                        Pair("b", items))
        ))
    }

    override fun getMetaEntityItemStack(itemStack: ItemStack): Any {
        return DataWatcher.WatchableObject(5, 6, CraftItemStack.asNMSCopy(itemStack))
    }

    override fun getMetaEntityProperties(onFire: Boolean, crouched: Boolean, sprinting: Boolean, swimming: Boolean, invisible: Boolean, glowing: Boolean, flyingElytra: Boolean): Any {
        var bits = 0
        bits += if (onFire) 1 else 0
        bits += if (crouched) 2 else 0
        bits += if (sprinting) 8 else 0
        bits += if (swimming) 10 else 0
        bits += if (invisible) 40 else 0
        return DataWatcher.WatchableObject(0, 0, bits.toByte())
    }

    override fun getMetaEntityGravity(noGravity: Boolean): Any {
        TODO()
    }

    override fun getMetaEntitySilenced(silenced: Boolean): Any {
        return DataWatcher.WatchableObject(0, 4, (if (silenced) 1 else 0).toByte())
    }

    override fun getMetaEntityCustomNameVisible(visible: Boolean): Any {
        return DataWatcher.WatchableObject(0, 3, (if (visible) 1 else 0).toByte())
    }

    override fun getMetaEntityCustomName(name: String): Any {
        return DataWatcher.WatchableObject(4, 2, name)
    }

    override fun getMetaArmorStandProperties(isSmall: Boolean, hasArms: Boolean, noBasePlate: Boolean, marker: Boolean): Any {
        var bits = 0
        bits += if (isSmall) 1 else 0
        bits += if (hasArms) 2 else 0
        bits += if (noBasePlate) 8 else 0
        bits += if (marker) 10 else 0
        return DataWatcher.WatchableObject(0, 10, bits.toByte())
    }

}