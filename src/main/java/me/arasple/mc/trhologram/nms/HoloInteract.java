package me.arasple.mc.trhologram.nms;

import io.izzel.taboolib.module.packet.Packet;
import io.izzel.taboolib.module.packet.TPacket;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.api.events.HologramInteractEvent;
import me.arasple.mc.trhologram.hologram.Hologram;
import me.arasple.mc.trhologram.hologram.HologramContent;
import org.bukkit.Bukkit;
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

                for (Hologram hologram : Hologram.getTrHolograms()) {
                    for (HologramContent line : hologram.getContents()) {
                        if (line.getId() == id) {
                            Bukkit.getScheduler().runTask(TrHologram.getPlugin(), () -> new HologramInteractEvent(player, hologram).call());
                            break;
                        }
                    }
                }
            }
        } catch (Throwable e) {
            TrHologram.LOGGER.error("An error occurred while processing hologram interact event. See the exception for details.");
            e.printStackTrace();
        }
        return true;
    }


}
