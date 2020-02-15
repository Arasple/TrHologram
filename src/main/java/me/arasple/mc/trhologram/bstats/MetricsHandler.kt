package me.arasple.mc.trhologram.bstats

import io.izzel.taboolib.module.inject.TSchedule
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.hologram.HologramManager
import org.bstats.bukkit.Metrics
import java.util.concurrent.Callable

/**
 * @author Arasple
 * @url https://bstats.org/plugin/bukkit/TrHologram
 */
object MetricsHandler {

    @TSchedule
    fun init() {

        val metrics = Metrics(TrHologram.getPlugin(), 6387)
        metrics.addCustomChart(Metrics.SingleLineChart("holograms", Callable { HologramManager.getHolograms().size }))
    }

}