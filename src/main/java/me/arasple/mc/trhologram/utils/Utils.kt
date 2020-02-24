package me.arasple.mc.trhologram.utils

import io.izzel.taboolib.internal.gson.JsonParser
import io.izzel.taboolib.util.item.Items
import io.izzel.taboolib.util.lite.Materials
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/2/24 16:52
 */
object Utils {

    fun loadItem(section: ConfigurationSection?): ItemStack {
        if (section != null) {
            val mat = section.getString("material")?.toUpperCase()?.replace(' ', '_')
            if (mat != null) {
                try {
                    Material.valueOf(mat)
                } catch (e: Throwable) {
                    section.set("material", Materials.valueOf(mat).parseMaterial()?.name)
                }
            }
        }
        val item = Items.loadItem(section)
        val meta = item.itemMeta
        meta?.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        item.itemMeta = meta
        return item
    }

    fun isJson(string: String): Boolean {
        return try {
            JsonParser.parseString(string).isJsonObject
        } catch (e: Throwable) {
            false
        }
    }

}