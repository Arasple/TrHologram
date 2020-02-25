package me.arasple.mc.trhologram.editor

import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.commands.CommandDelete
import me.arasple.mc.trhologram.editor.sub.ContentEditor
import me.arasple.mc.trhologram.hologram.Hologram
import me.arasple.mc.trhologram.utils.Locations
import me.arasple.mc.trhologram.utils.Utils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

/**
 * @author Arasple
 * @date 2020/2/15 12:38
 */
object EditorMenu {

    fun openEditor(hologram: Hologram, player: Player) {
        val nonPaper = Utils.nonPaper
        val menu =
                if (nonPaper) {
                    Bukkit.createInventory(Holder(hologram), 9, "Hologram - " + hologram.id)
                } else {
                    Bukkit.createInventory(Holder(hologram), InventoryType.HOPPER, "Hologram - " + hologram.id)
                }

        menu.setItem(if (nonPaper) 2 else 1, Utils.loadItem(TrHologram.SETTINGS.getConfigurationSection("GUIS.EDITOR.CONTENTS")))
        menu.setItem(if (nonPaper) 4 else 2, Utils.loadItem(TrHologram.SETTINGS.getConfigurationSection("GUIS.EDITOR.MOVE")))
        menu.setItem(if (nonPaper) 6 else 3, Utils.loadItem(TrHologram.SETTINGS.getConfigurationSection("GUIS.EDITOR.DELETE")))

        player.openInventory(menu)
        Sounds.BLOCK_CHEST_OPEN.playSound(player, 1f, 0f)
    }

    @TListener
    private class EventListener : Listener {

        @EventHandler(priority = EventPriority.HIGHEST)
        fun onClick(e: InventoryClickEvent) {
            val nonPaper = Utils.nonPaper

            if (e.inventory.holder is Holder) {
                val hologram = (e.inventory.holder as Holder).hologram
                e.isCancelled = true
                when (e.rawSlot) {
                    if (nonPaper) 2 else 1 -> ContentEditor.openEditor(hologram, e.whoClicked as Player)
                    if (nonPaper) 4 else 2 -> {
                        e.whoClicked.closeInventory()
                        hologram.updateLocation(Locations.getLocationForHologram(e.whoClicked as Player?))
                        Sounds.ENTITY_ENDERMAN_TELEPORT.playSound(hologram.getLocation())
                    }
                    if (nonPaper) 6 else 3 -> {
                        e.whoClicked.closeInventory()
                        CommandDelete.deleteHologram(e.whoClicked as Player, hologram.id)
                    }
                }
            }
        }

    }

    private class Holder(val hologram: Hologram) : InventoryHolder {

        override fun getInventory(): Inventory {
            TODO()
        }

    }

}