package me.arasple.mc.trhologram.api

import io.izzel.taboolib.kotlin.kether.KetherShell
import io.izzel.taboolib.kotlin.kether.common.util.LocalizedException
import me.arasple.mc.trhologram.api.hologram.HologramBuilder
import me.arasple.mc.trhologram.api.hologram.HologramComponent
import me.arasple.mc.trhologram.api.hologram.ItemHologram
import me.arasple.mc.trhologram.api.hologram.TextHologram
import me.arasple.mc.trhologram.module.display.Hologram
import me.arasple.mc.trhologram.module.service.Performance
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/2/10 9:38
 */
object TrHologramAPI {

    /**
     * 实体 ID 取得
     */
    private var INDEX = resetIndex()

    internal fun getIndex(): Int {
        return INDEX++
    }

    internal fun resetIndex(): Int {
        return 1197897763 + (0..7763).random()
    }

    @JvmStatic
    fun eval(player: Player, script: String): CompletableFuture<Any?> {
        Performance.MIRROR.check("Hologram:Handler:ScriptEval") {
            return try {
                KetherShell.eval(script, namespace = listOf("trhologram", "trmenu")) {
                    sender = player
                }
            } catch (e: LocalizedException) {
                println("[TrHologram] Unexpected exception while parsing kether shell:")
                e.localizedMessage.split("\n").forEach {
                    println("[TrHologram] $it")
                }
                CompletableFuture.completedFuture(false)
            }
        }
        throw Exception()
    }

    @JvmStatic
    fun getHologramById(id: String): Hologram? {
        return Hologram.holograms.find { it.id == id }
    }

    @JvmStatic
    fun createTextCompoent(
        initText: String,
        location: Location,
        tick: Long = -1,
        onTick: (HologramComponent) -> Unit = {}
    ): TextHologram {
        return TextHologram(initText, Position.fromLocation(location), tick, onTick)
    }

    @JvmStatic
    fun createItemCompoent(
        initItem: ItemStack,
        location: Location,
        tick: Long = -1,
        onTick: (HologramComponent) -> Unit = {}
    ): ItemHologram {
        return ItemHologram(initItem, Position.fromLocation(location), tick, onTick)
    }

    @JvmStatic
    fun builder(location: Location): HologramBuilder {
        return HologramBuilder(location = location)
    }


}