package me.arasple.mc.trhologram.edit.sub

import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.book.builder.BookBuilder
import io.izzel.taboolib.util.item.ItemBuilder
import io.izzel.taboolib.util.lite.Materials
import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.hologram.Hologram
import me.arasple.mc.trhologram.hologram.HologramManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEditBookEvent

/**
 * @author Arasple
 * @date 2020/2/15 12:54
 */
object ContentEditor {

    @Suppress("DEPRECATION")
    fun openEditor(hologram: Hologram, player: Player) {
        player.closeInventory()

        val book = ItemBuilder(BookBuilder(Materials.WRITABLE_BOOK.parseItem()).title("TrHologramEditor_" + hologram.name).author("TrHologramEditor_" + hologram.name).pagesRaw(hologram.rawContents).build()).name("&3编辑全全息图 &a" + hologram.name).lore("&7右键该书本即可打开编辑!").colored().build()
        player.inventory.addItem(book)
        Sounds.ENTITY_ITEM_PICKUP.playSound(player)
        TLocale.sendTo(player, "COMMANDS.EDIT.BOOK-EDIT")
    }

    @TListener
    private class EventListener : Listener {

        @EventHandler(priority = EventPriority.HIGHEST)
        fun onClick(e: PlayerEditBookEvent) {
            val author = e.newBookMeta.author
            val title = e.newBookMeta.title
            if (e.player.hasPermission("trhologram.admin") && author != null && title != null && author.startsWith("TrHologramEditor_") && title.startsWith("TrHologramEditor_") && author.substring(17) == title.substring(17)) {
                val hologram = TrHologramAPI.getHologramById(author.substring(17))
                if (hologram != null) {
                    hologram.setContents(e.newBookMeta.pages)
                    HologramManager.write(hologram)
                    Sounds.ITEM_BOTTLE_FILL.playSound(e.player)
                    TLocale.sendTo(e.player, "COMMANDS.EDIT.BOOK-EDIT-SUCCESS")
                }
            }
        }

    }

}