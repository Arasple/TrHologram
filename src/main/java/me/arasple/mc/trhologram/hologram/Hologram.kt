package me.arasple.mc.trhologram.hologram

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.lite.Sounds
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.action.ActionGroups
import me.arasple.mc.trhologram.action.TrAction
import me.arasple.mc.trhologram.utils.JavaScript
import me.arasple.mc.trhologram.utils.Locations
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.io.File

/**
 * @author Arasple
 * @date 2020/2/20 15:14
 */
class Hologram(var loadedFrom: String?, val id: String, private var loc: Location, private var contents: List<String>, var actions: MutableList<ActionGroups>, var viewCondition: String, private var viewDistance: String, private var finalViewDistance: Double) {

    val viewers: MutableList<Player> = mutableListOf()
    val lines: MutableList<HologramLine> = mutableListOf()

    init {
        updateLines(contents)
    }

    companion object {

        val HOLOGRAMS: MutableMap<String, MutableList<Hologram>> = mutableMapOf()

        fun createHologram(plugin: Plugin, id: String, loc: Location, contents: List<String>): Hologram {
            return createHologram(plugin, null, id, loc, contents, mutableListOf(), null, null)
        }

        fun createHologram(plugin: Plugin, id: String, loc: Location, contents: List<String>, actions: MutableList<ActionGroups>): Hologram {
            return createHologram(plugin, null, id, loc, contents, actions, null, null)
        }

        fun createHologram(plugin: Plugin, id: String, loc: Location, contents: List<String>, actions: MutableList<ActionGroups>, condition: String?, distance: String?): Hologram {
            return createHologram(plugin, null, id, loc, contents, actions, condition, distance)
        }

        fun createHologram(plugin: Plugin, file: File?, id: String, loc: Location, contents: List<String>, actions: MutableList<ActionGroups>, condition: String?, distance: String?): Hologram {
            val path = if (file == null || !file.exists()) null else file.path
            val holo = Hologram(path, id, loc, contents, actions, condition ?: "", distance
                    ?: "", NumberUtils.toDouble(distance, -1.0))
            HOLOGRAMS.putIfAbsent(plugin.name, mutableListOf())
            HOLOGRAMS[plugin.name]?.add(holo)
            return holo
        }

        fun getHolograms(): MutableList<Hologram> {
            return HOLOGRAMS.getOrDefault(TrHologram.getPlugin().name, mutableListOf())
        }

        fun display(player: Player) {
            HOLOGRAMS.values.forEach { list ->
                list.forEach { hologram ->
                    if (hologram.isVisible(player) && !hologram.viewers.contains(player)) {
                        hologram.display(player)
                    } else if (!hologram.isVisible(player) && hologram.viewers.contains(player)) {
                        hologram.destroy(player)
                        hologram.viewers.remove(player)
                    }
                }
            }
        }

        fun destroyFor(player: Player) {
            HOLOGRAMS.values.forEach { list ->
                list.forEach { hologram ->
                    hologram.removeViewer(player)
                }
            }
        }

    }

    fun display(vararg players: Player) {
        if (!loc.chunk.isLoaded) {
            return
        }
        for (player in players) {
            if (viewers.contains(player)) {
                lines.forEach { line -> line.update(player) }
            } else {
                refreshLines(player)
                viewers.add(player)
            }
        }
    }

    fun update() {
        if (!loc.chunk.isLoaded) {
            return
        }
        viewers.forEach { player: Player ->
            if (!isVisible(player)) {
                destroy(player)
            } else {
                lines.forEach { line -> line.update(player) }
            }
        }
        viewers.removeIf { player -> !player.isOnline }
    }

    fun destroy(player: Player): Boolean {
        lines.forEach { line -> line.destroy(player) }
        return true
    }

    fun destroyAll() {
        viewers.removeIf { player -> destroy(player) }
    }

    fun isVisible(player: Player): Boolean {
        if (player.location.world !== loc.world) {
            return false
        }
        val distance = if (finalViewDistance > 0) finalViewDistance else NumberUtils.toDouble(JavaScript.run(player, viewDistance).toString(), -1.0).coerceAtMost(70.0)
        return (distance <= 0 || player.location.distance(loc) <= distance) && (Strings.isEmpty(viewCondition) || JavaScript.run(player, viewCondition) as Boolean)
    }

    private fun refreshLines(player: Player) {
        var location: Location = loc.clone()
        val y = TrHologram.SETTINGS.getDouble("OPTIONS.ARMORSTAND-DISTANCE", 0.25)
        for (i in lines.indices) {
            val line: HologramLine = lines[i]
            // 若该行不为空行
            if (!line.isEmpty()) {
                // 若已生成盔甲架则摧毁
                if (line.hasArmortsandSpawned(player)) {
                    line.destroy(player)
                }
                // 显示
                line.display(location, player)
            } else {
                // 摧毁该空行的盔甲架
                viewers.forEach { p -> line.destroy(p) }
            }
            // 调整位置
            location = location.clone().subtract(0.0, if (line.mat != null) y * 3 else y, 0.0)
        }
    }

    fun delete() {
        destroyAll()
        lines.forEach { line -> line.cancelTask() }
        HOLOGRAMS.values.forEach { it -> it.removeIf { it == this } }
    }

    fun removeViewer(player: Player) {
        viewers.remove(player)
        lines.forEach { line -> line.viewing.remove(player.uniqueId) }
        destroy(player)
    }

    fun updateLines(contents: List<String>) {
        this.contents = contents
        while (contents.size < lines.size) {
            val line = lines[lines.size - 1]
            viewers.forEach { player -> line.destroy(player) }
            lines.remove(line)
        }
        val size: Int = lines.size
        for (i in contents.indices) {
            if (size > i) {
                val line = lines[i]
                if (line.mat == null) {
                    line.updateDisplayText(contents[i])
                } else {
                    viewers.forEach { player -> line.destroy(player) }
                    lines.remove(line)
                    lines.add(HologramLine(contents[i], this))
                }
            } else {
                lines.add(HologramLine(contents[i], this))
            }
        }
        viewers.forEach { player -> refreshLines(player) }
    }

    fun updateLocation(location: Location) {
        if (!Locations.equals(location, loc)) {
            loc = location
            viewers.forEach { refreshLines(it) }
            HologramManager.write(this, false)
        }
    }

    fun updateViewDistance(viewDistance: String) {
        this.viewDistance = viewDistance
        finalViewDistance = NumberUtils.toDouble(viewDistance, -1.0)
    }

    fun getRawContents(): List<String> {
        return contents
    }

    fun getViewDistance(): String {
        return viewDistance
    }

    fun getLocation(): Location {
        return loc
    }

    /*
    LOAD FROM FILE
     */

    fun initFromSection(data: YamlConfiguration) {
        loc = Locations.from(data.getString("location"))
        actions = TrAction.readActionGroups(data["actions"])
        viewCondition = data.getString("viewCondition")!!
        viewDistance = data.getString("viewDistance")!!
        updateLines(data.getStringList("contents"))
        if (TrHologram.SETTINGS.getBoolean("OPTIONS.AUTO-RELOAD-SOUND")) {
            if (Sounds.ITEM_BOTTLE_FILL.isSupported) {
                Sounds.ITEM_BOTTLE_FILL.playSound(loc, 1f, 0f)
            }
        }
    }

    fun initFromSection() {
        initFromSection(YamlConfiguration.loadConfiguration(File(loadedFrom)))
    }

}