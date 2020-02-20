package me.arasple.mc.trhologram.listeners

import io.izzel.taboolib.module.inject.TListener
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItem
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

/**
 * @author Arasple
 * @date 2020/2/20 22:53
 */
@TListener
class BugListener : Listener {

    @EventHandler
    fun onBug(e: PlayerDropItemEvent) {
//        e.itemDrop.setGravity(false)
//        e.player.sendMessage("Data::")
//        (e.itemDrop as CraftItem).handle.dataWatcher.b()?.forEach { data ->
//            e.player.sendMessage("->  " + data.d().toString())
//        }
    }

}