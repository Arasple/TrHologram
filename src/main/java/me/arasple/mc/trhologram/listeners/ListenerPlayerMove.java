package me.arasple.mc.trhologram.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trhologram.hologram.HologramManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Arasple
 * @date 2020/1/29 22:08
 */
@TListener
public class ListenerPlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        HologramManager.getHolograms().forEach(hologram -> {
            if (hologram.isVisible(player) && !hologram.getViewers().contains(player.getUniqueId())) {
                hologram.display(player);
            } else if (!hologram.isVisible(player) && hologram.getViewers().contains(player.getUniqueId())) {
                hologram.destroy(player);
            }
        });
    }

}
