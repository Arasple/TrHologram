package me.arasple.mc.trhologram.nms;

import io.izzel.taboolib.module.packet.Packet;
import io.izzel.taboolib.module.packet.TPacket;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Arasple
 * @date 2020/2/2 17:07
 */
public class HoloDirection {

    @TPacket(type = TPacket.Type.RECEIVE)
    static boolean interactEntity(Player player, Packet packet) {
        if (packet != null && player.isOnline()) {
            if (packet.is("PacketPlayInLook") || packet.is("PacketPlayInPositionLook")) {
                Vector lookDirection = HoloPackets.getInst().getLookDirection(packet);
            }
        }
        return true;
    }

}
