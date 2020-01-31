package me.arasple.mc.trhologram.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trhologram.hologram.HologramManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Arasple
 * @date 2020/1/30 13:11
 */
@TListener
public class ListenerPlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        HologramManager.getHolograms().stream().filter(hologram -> hologram.isVisible(player)).forEach(hologram -> hologram.display(player));
    }

}
