package me.arasple.mc.trhologram.nms;

import io.izzel.taboolib.module.packet.Packet;
import io.izzel.taboolib.module.packet.TPacket;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.api.events.HologramInteractEvent;
import me.arasple.mc.trhologram.hologram.Hologram;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/29 22:16
 */
public class HoloInteract {

    @TPacket(type = TPacket.Type.RECEIVE)
    static boolean interactEntity(Player player, Packet packet) {
        try {
            if (packet == null || player == null || !player.isOnline()) {
                return true;
            }
            if (packet.is("PacketPlayInUseEntity")) {
                int id = (int) packet.read("a");
                Hologram hologram = TrHologramAPI.getHologramByEntityId(id);
                if (hologram != null) {
                    new HologramInteractEvent(player, hologram).call();
                }
            }
        } catch (Throwable ignored) {
        }
        return true;
    }

}
