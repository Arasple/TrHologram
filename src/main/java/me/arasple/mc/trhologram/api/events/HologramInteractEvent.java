package me.arasple.mc.trhologram.api.events;

import me.arasple.mc.trhologram.hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author Arasple
 * @date 2020/1/29 22:20
 */
public class HologramInteractEvent extends PlayerEvent {


    private static final HandlerList HANDLERS = new HandlerList();
    private Hologram hologram;

    public HologramInteractEvent(Player who, Hologram hologram) {
        super(who);
        this.hologram = hologram;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public void call() {
        Bukkit.getPluginManager().callEvent(this);
    }

    public Hologram getHologram() {
        return hologram;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
