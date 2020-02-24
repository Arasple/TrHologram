package me.arasple.mc.trhologram.nms.impl

import io.izzel.taboolib.module.lite.SimpleReflection
import io.izzel.taboolib.module.packet.TPacketHandler
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.nms.HoloPackets
import net.minecraft.server.v1_12_R1.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author Arasple
 * @date 2020/2/1 22:40
 */
class HoloPacketsImp_v1_12 : HoloPackets() {

    companion object {
        init {
            SimpleReflection.checkAndSave(
                    PacketPlayOutSpawnEntity::class.java,
                    PacketPlayOutEntityTeleport::class.java,
                    PacketPlayOutEntityMetadata::class.java,
                    PacketPlayOutMount::class.java
            )
        }
    }

    override fun spawnArmorStand(player: Player, entityId: Int, uuid: UUID, location: Location) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutSpawnEntity::class.java, PacketPlayOutSpawnEntity(),
                mapOf(
                        Pair("a", entityId),
                        Pair("b", uuid),
                        Pair("c", location.x),
                        Pair("d", location.y),
                        Pair("e", location.z),
                        Pair("f", 0),
                        Pair("g", 0),
                        Pair("h", 0),
                        Pair("i", 0),
                        Pair("j", 0),
                        Pair("k", 78),
                        Pair("l", 0)
                )
        ))
        initArmorStandAsHologram(player, entityId)
    }

    override fun spawnItem(player: Player, entityId: Int, uuid: UUID, location: Location, itemStack: ItemStack) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutSpawnEntity::class.java, PacketPlayOutSpawnEntity(),
                mapOf(
                        Pair("a", entityId),
                        Pair("b", uuid),
                        Pair("c", location.x),
                        Pair("d", location.y),
                        Pair("e", location.z),
                        Pair("f", 0),
                        Pair("g", 0),
                        Pair("h", 0),
                        Pair("i", 0),
                        Pair("j", 0),
                        Pair("k", 2),
                        Pair("l", 0)
                )
        ))
        sendEntityMetadata(player, entityId, getMetaEntityGravity(true), getMetaEntityItemStack(itemStack))
    }

    override fun destroyEntity(player: Player, entityId: Int) {
        TPacketHandler.sendPacket(player, PacketPlayOutEntityDestroy(entityId))
    }

    override fun initArmorStandAsHologram(player: Player, entityId: Int) {
        sendEntityMetadata(player, entityId,
                getMetaEntityGravity(false),
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
                        Pair("b", location.x),
                        Pair("c", location.y),
                        Pair("d", location.z),
                        Pair("e", 0),
                        Pair("f", 0),
                        Pair("g", false)
                )
        ))
    }

    override fun updatePassengers(player: Player, entityId: Int, vararg passengers: Int) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutMount::class.java, PacketPlayOutMount(),
                mapOf(
                        Pair("a", entityId),
                        Pair("b", passengers)
                )
        ))
    }

    override fun updateArmorStandEquipmentItem(player: Player, entityId: Int, slot: EquipmentSlot, itemStack: ItemStack) {
        TPacketHandler.sendPacket(player, PacketPlayOutEntityEquipment(entityId, EnumItemSlot.valueOf(slot.name), CraftItemStack.asNMSCopy(itemStack)))
    }

    override fun sendEntityMetadata(player: Player, entityId: Int, vararg objects: Any) {
        val items: MutableList<DataWatcher.Item<*>> = ArrayList()
        for (obj in objects) {
            items.add(obj as DataWatcher.Item<*>)
        }
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutEntityMetadata::class.java, PacketPlayOutEntityMetadata(),
                mapOf(
                        Pair("a", entityId),
                        Pair("b", items))
        ))
    }

    override fun getMetaEntityItemStack(itemStack: ItemStack): Any {
        return DataWatcher.Item(DataWatcherObject(6, DataWatcherRegistry.f), CraftItemStack.asNMSCopy(itemStack))
    }

    override fun getMetaEntityProperties(onFire: Boolean, crouched: Boolean, sprinting: Boolean, swimming: Boolean, invisible: Boolean, glowing: Boolean, flyingElytra: Boolean): Any {
        var bits = 0
        bits += if (onFire) 1 else 0
        bits += if (crouched) 2 else 0
        bits += if (sprinting) 8 else 0
        bits += if (swimming) 10 else 0
        bits += if (glowing) 20 else 0
        bits += if (invisible) 40 else 0
        bits += if (flyingElytra) 80 else 0
        return DataWatcher.Item(DataWatcherObject(0, DataWatcherRegistry.a), bits.toByte())
    }

    override fun getMetaEntityGravity(noGravity: Boolean): Any {
        return DataWatcher.Item(DataWatcherObject(5, DataWatcherRegistry.h), noGravity)
    }

    override fun getMetaEntitySilenced(silenced: Boolean): Any {
        return DataWatcher.Item(DataWatcherObject(4, DataWatcherRegistry.h), silenced)
    }

    override fun getMetaEntityCustomNameVisible(visible: Boolean): Any {
        return DataWatcher.Item(DataWatcherObject(3, DataWatcherRegistry.h), visible)
    }

    override fun getMetaEntityCustomName(name: String): Any {
        return DataWatcher.Item(DataWatcherObject(2, DataWatcherRegistry.d), name)
    }

    override fun getMetaArmorStandProperties(isSmall: Boolean, hasArms: Boolean, noBasePlate: Boolean, marker: Boolean): Any {
        var bits = 0
        bits += if (isSmall) 1 else 0
        bits += if (hasArms) 4 else 0
        bits += if (noBasePlate) 8 else 0
        bits += if (marker) 10 else 0
        return DataWatcher.Item(DataWatcherObject(11, DataWatcherRegistry.a), bits.toByte())
    }

}