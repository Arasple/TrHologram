package me.arasple.mc.trhologram.module.editor

import io.izzel.taboolib.cronus.CronusUtils
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.book.builder.BookBuilder
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.lite.Materials
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.api.hologram.ItemHologram
import me.arasple.mc.trhologram.api.hologram.TextHologram
import me.arasple.mc.trhologram.module.conf.HologramLoader
import me.arasple.mc.trhologram.module.display.Hologram
import me.arasple.mc.trhologram.module.display.texture.Texture
import me.arasple.mc.trhologram.util.Parser
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEditBookEvent

/**
 * @author Arasple
 * @date 2021/2/12 17:09
 */
@TListener
object Editor : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onBookEdit(e: PlayerEditBookEvent) {
        val lore = e.newBookMeta.lore
        if (lore !== null && lore.getOrNull(0) == "§8TrHologram Editor") {
            val hologram = TrHologramAPI.getHologramById(lore[1].removePrefix("§0")) ?: return
            val book = e.newBookMeta.pages

            Items.takeItem(e.player.inventory, { Items.hasLore(it, "TrHologram Editor") }, 99)
        }
    }

    fun contentEditor(player: Player, hologram: Hologram) {
        CronusUtils.addItem(
            player,
            ItemBuilder(
                BookBuilder(Materials.WRITABLE_BOOK.parseItem())
                    .author("Arasple")
                    .title("TrHologram Editor")
                    .pagesRaw(
                        hologram.components.map {
                            when (it) {
                                is TextHologram -> it.text
                                is ItemHologram -> (it.display as Texture).raw
                                else -> "__ERROR__"
                            }
                        }
                    )
                    .build()
            )
                .name("Editor: ${hologram.id}")
                .lore("§8TrHologram Editor", "§0${hologram.id}")
                .build()
        )
    }

    fun edit(player: Player, hologram: Hologram) {

        val plane =
            TellrawJson
                .create()
                .newLine()
                .append("      §7§lHOLOGRAM EDITOR").newLine()
                .append("      §3§l§n${hologram.id}").newLine()
                .newLine()
                .append("      §fPosition: §2${hologram.position} ").append("§8(§6•§8)")
                .hoverText("Move to your location")
                .clickCommand("/trhologram movehere ${hologram.id}")
                .newLine()
                .append("      §fView-Distance: §e${hologram.viewDistance}").newLine()
                .append("      §fView-Condition: §2${hologram.viewCondition ?: "*"}").newLine()
                .append("      §fRefresh-Condition: §2${hologram.refreshCondition} ticks")
                .newLine()
                .newLine()
                .append("      §6Components:")
                .newLine()

        hologram.components.forEachIndexed { index, it ->
            when (it) {
                is TextHologram ->
                    plane
                        .append("        §8- §7${it.text}")
                        .hoverText(Parser.parseString(player, it.text))
                        .clickSuggest("/trhd edit ${hologram.id} line " + it.text)
                        .newLine()
                is ItemHologram -> {
                    val item = it.display.generate(player)
                    plane
                        .append("        §8- §8[ §7${Items.getName(item)} §8]")
                        .hoverItem(item)
                        .newLine()
                }
            }
        }

        plane.send(player)
    }

}