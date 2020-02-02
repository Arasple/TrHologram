package me.arasple.mc.trhologram.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trhologram.hologram.HologramManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Arasple
 * @date 2020/1/30 13:11
 */
@TListener
public class ListenerPlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        HologramManager.getHolograms().forEach(hologram -> hologram.getViewers().remove(player.getUniqueId()));
    }

}
