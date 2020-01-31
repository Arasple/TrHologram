package me.arasple.mc.trhologram.bstats;

import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.hologram.HologramManager;
import org.bstats.bukkit.Metrics;

/**
 * @author Arasple
 * @url https://bstats.org/plugin/bukkit/TrHologram
 */
public class MetricsHandler {

    @TSchedule
    public static void init() {
        Metrics metrics = new Metrics(TrHologram.getPlugin(), 6387);
        metrics.addCustomChart(new Metrics.SingleLineChart("holograms", () -> HologramManager.getHolograms().size()));
    }

}