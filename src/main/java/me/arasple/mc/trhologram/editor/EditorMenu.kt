package me.arasple.mc.trhologram.editor

import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.commands.CommandDelete
import me.arasple.mc.trhologram.editor.sub.ContentEditor
import me.arasple.mc.trhologram.hologram.Hologram
import me.arasple.mc.trhologram.utils.Locations
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
        val menu = Bukkit.createInventory(Holder(hologram), InventoryType.HOPPER, "管理全息图 " + hologram.name)
        menu.setItem(1, Items.loadItem(TrHologram.SETTINGS.getConfigurationSection("GUIS.EDITOR.CONTENTS")))
        menu.setItem(2, Items.loadItem(TrHologram.SETTINGS.getConfigurationSection("GUIS.EDITOR.MOVE")))
        menu.setItem(3, Items.loadItem(TrHologram.SETTINGS.getConfigurationSection("GUIS.EDITOR.DELETE")))

        player.openInventory(menu)
        Sounds.BLOCK_CHEST_OPEN.playSound(player, 1f, 0f)
    }

    @TListener
    private class EventListener : Listener {

        @EventHandler(priority = EventPriority.HIGHEST)
        fun onClick(e: InventoryClickEvent) {
            if (e.inventory.holder is Holder) {
                val hologram = (e.inventory.holder as Holder).hologram
                e.isCancelled = true
                when (e.rawSlot) {
                    1 -> ContentEditor.openEditor(hologram, e.whoClicked as Player)
                    2 -> {
                        e.whoClicked.closeInventory()
                        hologram.location = Locations.getLocationForHologram(e.whoClicked as Player?)
                        Sounds.ENTITY_ENDERMAN_TELEPORT.playSound(hologram.location)
                    }
                    3 -> {
                        e.whoClicked.closeInventory()
                        CommandDelete.deleteHologram(e.whoClicked as Player, hologram.name)
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