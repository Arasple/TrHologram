package me.arasple.mc.trhologram.module.service

import io.izzel.taboolib.module.inject.TFunction
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.module.display.Hologram
import org.bstats.bukkit.Metrics

/**
 * @author Arasple
 * @date 2020/3/7 22:15
 */
object Metrics {

    private val B_STATS by lazy { Metrics(TrHologram.plugin, 6387) }

    @TFunction.Init
    fun initialization() {
        B_STATS.let {
            it.addCustomChart(Metrics.SingleLineChart("holograms") {
                Hologram.holograms.size + Hologram.externalHolograms.size
            })
        }
    }

}