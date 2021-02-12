package me.arasple.mc.trhologram.api.nms

import com.mojang.authlib.GameProfile
import com.mojang.datafixers.util.Pair
import io.izzel.taboolib.Version
import io.izzel.taboolib.module.lite.SimpleEquip
import io.izzel.taboolib.util.item.Equipments
import io.izzel.taboolib.util.item.Items
import me.arasple.mc.trhologram.api.nms.packet.*
import net.minecraft.server.v1_16_R1.*
import net.minecraft.server.v1_16_R3.ChatComponentText
import net.minecraft.server.v1_16_R3.EntityTypes
import net.minecraft.server.v1_16_R3.PacketPlayOutMount
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.*

/**
 * @author Arasple
 * @date 2020/12/4 21:25
 */
class NMSImpl : NMS() {

    private val version = Version.getCurrentVersionInt()
    private val emptyItemStack = CraftItemStack.asNMSCopy((ItemStack(Material.AIR)))
    private val indexs = arrayOf(
        // armorstand
        arrayOf(11500 to 14, 11400 to 13, 11000 to 11, 10900 to 10),
        // item
        arrayOf(11300 to 7, 11000 to 6, 10900 to 5),
    ).map { it -> it.firstOrNull { version >= it.first }?.second ?: -1 }

    /**
     * 处理实体封装后的数据包
     */
    override fun sendEntityPacket(player: Player, vararg packets: PacketEntity) {
        packets.forEach {
            val id = it.entityId

            when (it) {
                // Destroy entity
                is PacketEntityDestroy -> sendPacket(player, PacketPlayOutEntityDestroy(id))
                // Spawn Armor Stand
                is PacketEntitySpawn -> {
                    sendPacket(
                        player,
                        PacketPlayOutSpawnEntity(),
                        "a" to id,
                        "b" to (it.uuid ?: UUID.randomUUID()),
                        "c" to it.position.x,
                        "d" to it.position.y,
                        "e" to it.position.z,
                        "f" to 0.toByte(),
                        "g" to 0.toByte(),
                        "k" to if (it.type) if (version >= 11400) EntityTypes.ARMOR_STAND else 78 else if (version >= 11400) EntityTypes.ITEM else 2
                    )
                    // Cancel graivity
                    sendEntityMetadata(
                        player,
                        id,
                        getMetaEntityBoolean(5, true)
                    )
                }
                is PacketEntityMount -> {
                    sendPacket(player, PacketPlayOutMount(), "a" to it.entityId, "b" to it.mount)
                }
                // Packet Modify
                is PacketArmorStandModify -> {
                    var entity = 0
                    var armorstand = 0
                    if (it.isInvisible) entity += 0x20.toByte()
                    if (it.isGlowing) entity += 0x40.toByte()
                    if (it.isSmall) armorstand += 0x01.toByte()
                    if (it.hasArms) armorstand += 0x04.toByte()
                    if (it.noBasePlate) armorstand += 0x08.toByte()
                    if (it.isMarker) armorstand += 0x10.toByte()
                    sendEntityMetadata(
                        player, id,
                        getMetaEntityByte(0, entity.toByte()),
                        getMetaEntityByte(indexs[0], armorstand.toByte()),
                    )
                }
                // Modify Item
                is PacketItemModify -> {
                    var entity = 0
                    val itemNull = Items.isNull(it.itemStack)
                    if (it.isInvisible || itemNull) entity += 0x80.toByte()
                    if (it.isGlowing) entity += 0x40.toByte()

                    sendEntityMetadata(player, id, getMetaEntityByte(0, entity.toByte()))
                    if (!itemNull) sendEntityMetadata(player, id, getMetaItem(indexs[1], it.itemStack))
                }
                // Entity Name
                is PacketArmorStandName -> {
                    sendEntityMetadata(
                        player, id,
                        getMetaEntityChatBaseComponent(2, it.name),
                        getMetaEntityBoolean(3, it.isCustomNameVisible)
                    )
                }
                is PacketEquipment -> updateEquipment(player, id, it.slot, it.itemStack)
            }
        }

    }

    /**
     * 更新实体属性 Metadata
     */
    override fun sendEntityMetadata(player: Player, entityId: Int, vararg objects: Any) {
        sendPacket(
            player,
            PacketPlayOutEntityMetadata(),
            "a" to entityId,
            "b" to objects.map { it as DataWatcher.Item<*> }.toList()
        )
    }

    override fun parseVec3d(obj: Any): Vector {
        return Vector((obj as Vec3D).x, obj.y, obj.z)
    }

    override fun getGameProfile(player: Player): GameProfile {
        return (player as CraftPlayer).profile
    }

    /**
     * 私有方法 & NMS 相关处理
     */

    private fun updateEquipment(player: Player, entityId: Int, slot: EquipmentSlot, itemStack: ItemStack) {
        when {
            version >= 11600 -> {
                sendPacket(
                    player,
                    PacketPlayOutEntityEquipment(
                        entityId,
                        listOf(
                            Pair(
                                EnumItemSlot.fromName(SimpleEquip.fromBukkit(slot).nms),
                                org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asNMSCopy(itemStack)
                            )
                        )
                    )
                )
            }
            version >= 11300 -> {
                sendPacket(
                    player,
                    net.minecraft.server.v1_13_R2.PacketPlayOutEntityEquipment(
                        entityId,
                        net.minecraft.server.v1_13_R2.EnumItemSlot.fromName(Equipments.fromBukkit(slot)?.nms),
                        org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(itemStack)
                    )
                )
            }
            else -> {
                sendPacket(
                    player,
                    net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment(
                        entityId,
                        net.minecraft.server.v1_12_R1.EnumItemSlot.a(Equipments.fromBukkit(slot)?.nms),
                        org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(itemStack)
                    )
                )
            }
        }
    }


    private fun getMetaEntityByte(index: Int, value: Byte): Any {
        return DataWatcher.Item(DataWatcherObject(index, DataWatcherRegistry.a), value)
    }

    private fun getMetaEntityBoolean(index: Int, value: Boolean): Any {
        return if (version >= 11300) {
            DataWatcher.Item(DataWatcherObject(index, DataWatcherRegistry.i), value)
        } else {
            net.minecraft.server.v1_12_R1.DataWatcher.Item(
                net.minecraft.server.v1_12_R1.DataWatcherObject(
                    index,
                    net.minecraft.server.v1_12_R1.DataWatcherRegistry.h
                ), value
            )
        }
    }

    private fun getMetaEntityChatBaseComponent(index: Int, name: String?): Any {
        return if (version >= 11300) {
            DataWatcher.Item<Optional<IChatBaseComponent>>(
                DataWatcherObject(index, DataWatcherRegistry.f),
                Optional.ofNullable(
                    (if (name == null) null else toChatBaseComponent(name, true)) as IChatBaseComponent?
                )
            )
        } else {
            net.minecraft.server.v1_12_R1.DataWatcher.Item(
                net.minecraft.server.v1_12_R1.DataWatcherObject(
                    index,
                    net.minecraft.server.v1_12_R1.DataWatcherRegistry.d
                ), name ?: ""
            )
        }
    }

    private fun getMetaItem(index: Int, itemStack: ItemStack): Any {
        return when {
            version >= 11300 -> {
                DataWatcher.Item(
                    DataWatcherObject(index, DataWatcherRegistry.g),
                    org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asNMSCopy(itemStack)
                )
            }
            version >= 11200 -> {
                net.minecraft.server.v1_12_R1.DataWatcher.Item(
                    net.minecraft.server.v1_12_R1.DataWatcherObject(
                        6,
                        net.minecraft.server.v1_12_R1.DataWatcherRegistry.f
                    ), org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(itemStack)
                )
            }
            else -> {
                return net.minecraft.server.v1_9_R2.DataWatcher.Item(
                    net.minecraft.server.v1_9_R2.DataWatcherObject(
                        6,
                        net.minecraft.server.v1_9_R2.DataWatcherRegistry.f
                    ),
                    com.google.common.base.Optional.fromNullable(
                        org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack.asNMSCopy(
                            itemStack
                        )
                    )
                )
            }
        }
    }

    private fun toChatBaseComponent(string: String, craftChatMessage: Boolean): Any {
        return if (craftChatMessage) CraftChatMessage.fromString(string).first()
        else ChatComponentText(string)
    }

    private fun toNMSItemStack(vararg itemStack: ItemStack?): Any {
        if (itemStack.size > 1) {
            return itemStack.map { asNMSCopy(it) }
        }
        return asNMSCopy(itemStack[0])
    }

    private fun asNMSCopy(itemStack: ItemStack?): Any {
        return if (itemStack == null || itemStack.type == Material.AIR) emptyItemStack
        else CraftItemStack.asNMSCopy(itemStack)
    }

}