package me.arasple.mc.trhologram.nms

import io.izzel.taboolib.Version
import io.izzel.taboolib.module.lite.SimpleReflection
import io.izzel.taboolib.module.lite.SimpleVersionControl
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trhologram.TrHologram
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author Arasple
 * @date 2020/2/15 21:02
 * -
 * 部分代码参考自项目
 * https://github.com/VolmitSoftware/Mortar
 */
abstract class HoloPackets {

    abstract fun spawnArmorStand(player: Player, entityId: Int, uuid: UUID, location: Location)
    abstract fun spawnItem(player: Player, entityId: Int, uuid: UUID, location: Location, itemStack: ItemStack)
    abstract fun destroyEntity(player: Player, entityId: Int)
    abstract fun initArmorStandAsHologram(player: Player, entityId: Int)
    abstract fun updateArmorStandDisplayName(player: Player, entityId: Int, name: String)
    abstract fun updateArmorStandLocation(player: Player, entityId: Int, location: Location)
    abstract fun updatePassengers(player: Player, entityId: Int, vararg passengers: Int)
    abstract fun updateArmorStandEquipmentItem(player: Player, entityId: Int, slot: EquipmentSlot, itemStack: ItemStack)
    abstract fun sendEntityMetadata(player: Player, entityId: Int, vararg objects: Any)
    abstract fun getMetaEntityItemStack(itemStack: ItemStack): Any
    abstract fun getMetaEntityProperties(onFire: Boolean, crouched: Boolean, sprinting: Boolean, swimming: Boolean, invisible: Boolean, glowing: Boolean, flyingElytra: Boolean): Any
    abstract fun getMetaEntityGravity(noGravity: Boolean): Any
    abstract fun getMetaEntitySilenced(silenced: Boolean): Any
    abstract fun getMetaEntityCustomNameVisible(visible: Boolean): Any
    abstract fun getMetaEntityCustomName(name: String): Any
    abstract fun getMetaArmorStandProperties(isSmall: Boolean, hasArms: Boolean, noBasePlate: Boolean, marker: Boolean): Any

    fun setPacket(nms: Class<*>, packet: Any, sets: Map<String, Any>): Any {
        sets.forEach { (key: String, value: Any) -> SimpleReflection.setFieldValue(nms, packet, key, value) }
        return packet
    }

    companion object {

        var INSTANCE: HoloPackets? = null

        fun init() {
            try {
                val version =
                        when {
                            Version.isAfter(Version.v1_13) -> Version.v1_15
                            Version.isAfter(Version.v1_11) -> Version.v1_12
                            Version.isAfter(Version.v1_9) -> Version.v1_9
                            Version.isAfter(Version.v1_8) -> Version.v1_8
                            else -> Version.vNull
                        }
                if (version == Version.vNull) {
                    TLocale.sendToConsole("PLUGIN.UN-SUPPORTED-VERSION", Bukkit.getVersion())
                    Bukkit.getPluginManager().disablePlugin(TrHologram.getPlugin())
                    return
                }
                INSTANCE = SimpleVersionControl.createNMS("me.arasple.mc.trhologram.nms.impl.HoloPacketsImp_${version}").translate(TrHologram.getPlugin()).newInstance() as HoloPackets
                TLocale.sendToConsole("PLUGIN.LOADING", Bukkit.getVersion())
            } catch (e: Throwable) {
                TrHologram.LOGGER.error("An error occurred while adapting your server version " + Version.getBukkitVersion() + ", please make sure your version is supported. Plugin will not work")
                e.printStackTrace()
            }
        }

    }
}
