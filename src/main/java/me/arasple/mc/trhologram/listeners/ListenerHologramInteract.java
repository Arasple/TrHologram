package me.arasple.mc.trhologram.listeners;

import io.izzel.taboolib.module.inject.TListener;
import me.arasple.mc.trhologram.action.TrAction;
import me.arasple.mc.trhologram.api.events.HologramInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * @author Arasple
 * @date 2020/2/1 11:18
 */
@TListener
public class ListenerHologramInteract implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHologramInteract(HologramInteractEvent e) {
        Player player = e.getPlayer();
        TrAction.runActions(e.getHologram().getActions(), player);
    }

}
