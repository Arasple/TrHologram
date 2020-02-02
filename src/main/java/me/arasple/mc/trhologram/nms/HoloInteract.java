package me.arasple.mc.trhologram.nms;

import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.packet.Packet;
import io.izzel.taboolib.module.packet.TPacket;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.api.events.HologramInteractEvent;
import me.arasple.mc.trhologram.hologram.Hologram;
import me.arasple.mc.trhologram.hologram.HologramContent;
import me.arasple.mc.trhologram.hologram.HologramManager;
import me.arasple.mc.trhologram.trace.RayTrace;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Arasple
 * @date 2020/1/29 22:16
 */
@TListener
public class HoloInteract implements Listener {

    @TPacket(type = TPacket.Type.RECEIVE)
    static boolean interactEntity(Player player, Packet packet) {
        try {
            if (packet == null || player == null || !player.isOnline()) {
                return true;
            }
            if (packet.is("PacketPlayInUseEntity")) {
                int id = (int) packet.read("a");
                HologramContent hologramContent = TrHologramAPI.getHologramContentByEntityId(id);
                Hologram hologram = TrHologramAPI.getHologramByEntityId(id);
                if (hologram != null) {
                    new HologramInteractEvent(player, hologram).call();
                    player.sendMessage("hologramContent: " + hologramContent.getText());
                }
            }
        } catch (Throwable ignored) {
        }
        return true;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        new RayTrace(player.getEyeLocation(), player.getLocation().getDirection(), 10.0D, 0.5D) {
            @Override
            public void onTrace(Location location) {
                HologramManager.getHolograms().stream().filter(hologram -> hologram.isViewing(player)).forEach(hologram -> {
                    hologram.getContents().forEach(content -> {
                        if (content.getLocation().distanceSquared(location) < 3.0D) {
                            player.sendMessage("Tracked: " + content.getText());
                            stop();
                        }
                    });
                });
            }
        }.trace();
    }


}
