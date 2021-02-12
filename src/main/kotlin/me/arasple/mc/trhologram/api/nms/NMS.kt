package me.arasple.mc.trhologram.api.nms

import com.mojang.authlib.GameProfile
import io.izzel.taboolib.kotlin.Reflex
import io.izzel.taboolib.module.packet.TPacketHandler
import io.izzel.taboolib.util.asm.AsmVersionControl
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.api.nms.packet.PacketEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * @author Arasple
 * @date 2020/12/4 21:20
 */
abstract class NMS {

    companion object {

        /**
         * @see NMSImpl
         */
        val INSTANCE: NMS =
            AsmVersionControl.createNMS("me.arasple.mc.trhologram.api.nms.NMSImpl").translate(TrHologram.plugin)
                .newInstance() as NMS

    }

    abstract fun sendEntityPacket(player: Player, vararg packets: PacketEntity)

    abstract fun sendEntityMetadata(player: Player, entityId: Int, vararg objects: Any)

    abstract fun parseVec3d(obj: Any): Vector

    fun sendPacket(player: Player, packet: Any, vararg fields: Pair<Any, Any>) {
        TPacketHandler.sendPacket(player, Reflex.of(packet).let { inst ->
            fields.forEach { inst.set(it.first.toString(), it.second) }
            inst.instance
        })
    }

    abstract fun getGameProfile(player: Player): GameProfile

}