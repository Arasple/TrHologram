package me.arasple.mc.trhologram.module.conf

import io.izzel.taboolib.util.Files
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.api.Position
import me.arasple.mc.trhologram.api.Settings
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.api.event.HologramInteractEvent.Type
import me.arasple.mc.trhologram.api.hologram.HologramComponent
import me.arasple.mc.trhologram.api.hologram.ItemHologram
import me.arasple.mc.trhologram.api.hologram.TextHologram
import me.arasple.mc.trhologram.module.action.ClickReaction
import me.arasple.mc.trhologram.module.action.Reaction
import me.arasple.mc.trhologram.module.condition.Condition
import me.arasple.mc.trhologram.module.display.Hologram
import me.arasple.mc.trhologram.module.display.texture.Texture
import me.arasple.mc.trhologram.util.Parser
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * @author Arasple
 * @date 2021/2/11 10:03
 */
object HologramLoader {

    private val folder = Files.file(TrHologram.plugin.dataFolder, "holograms")

    fun create(id: String, location: Location): Hologram {
        val hologram =
            """
            Location: ${Parser.fromLocation(location)}

            Contents:
              - '&7Hello, &2Tr&aHologram&7!'
              - '{item: emerald}'
              - ''
              - '&3Nice to meet you, &a{{player name}}'

            Actions:
              All: 'tell color *&bHi, you just clicked this hologram'
            """.trimIndent()

        Files.file(folder, "$id.yml").also {
            it.writeText(hologram, StandardCharsets.UTF_8)
            return load(it)
        }
    }

    fun load(): Int {
        Hologram.clear()
        TrHologramAPI.resetIndex()

        filterHologramFiles(folder).forEach { load(it) }
        Settings.INSTANCE.loadPaths.flatMap { filterHologramFiles(File(it)) }.forEach { load(it) }

        return Hologram.holograms.size
    }

    fun load(file: File): Hologram {
        val id = file.nameWithoutExtension
        val conf = YamlConfiguration.loadConfiguration(file)
        val location = Parser.parseLocation(conf.getString("Location") ?: throw Exception("No valid location"))
        val lineSpacing = conf.getDouble("Options.Line-Spacing", Settings.INSTANCE.lineSpacing)
        val viewDistance =
            conf.getDouble("Options.View-Distance", Settings.INSTANCE.viewDistance).coerceAtMost(50.0)
        val viewCondition = conf.getString("Options.View-Condition", Settings.INSTANCE.viewCondition).let {
            if (it == null || it.isBlank()) null
            else Condition(it)
        }
        val refreshCondition = conf.getLong("Options.Refresh-Condition", Settings.INSTANCE.refershCondition)
        val contents = conf.getStringList("Contents").ifEmpty { listOf("TrHologram") }
        val actions = mutableMapOf<Type, Reaction>()

        val all = conf.getStringList("Actions.All")
        val left = conf.getStringList("Actions.Left")
        val shiftLeft = conf.getStringList("Actions.Shift_Left")
        val right = conf.getStringList("Actions.Right")
        val shiftRight = conf.getStringList("Actions.Shift_Right")

        if (all.isNotEmpty()) actions[Type.ALL] = Reaction(all)
        if (left.isNotEmpty()) actions[Type.LEFT] = Reaction(left)
        if (shiftLeft.isNotEmpty()) actions[Type.SHIFT_LEFT] = Reaction(shiftLeft)
        if (right.isNotEmpty()) actions[Type.RIGHT] = Reaction(right)
        if (shiftRight.isNotEmpty()) actions[Type.SHIFT_RIGHT] = Reaction(shiftRight)

        val holograms = mutableListOf<HologramComponent>()
        var position = Position.fromLocation(location).clone(y = -1.25)

        contents.forEach {
            val (line, options) = Property.from(it)
            val itemDisplay = options[Property.ITEM]
            val update = options[Property.UPDATE]?.toLongOrNull() ?: -1
            val offset = options[Property.OFFSET]?.toDoubleOrNull() ?: lineSpacing
            val isItem = itemDisplay != null
            val fix = if (isItem) 0.5 else 0.0

            position = position.clone(y = -(fix + offset))

            val hologram = when {
                isItem -> ItemHologram(Texture.createTexture(itemDisplay!!), position, update)
                line.isBlank() -> null
                else -> TextHologram(line, position, update)
            }

            if (hologram != null) holograms.add(hologram)
        }

        // Loaded & Add
        val hologram = Hologram(
            id,
            Position.fromLocation(location),
            viewDistance,
            viewCondition,
            refreshCondition,
            holograms,
            ClickReaction(actions)
        )
        Hologram.holograms.add(hologram)
        return hologram
    }

    private fun filterHologramFiles(file: File): List<File> {
        return mutableListOf<File>().run {
            if (file.isDirectory) {
                file.listFiles()?.forEach {
                    addAll(filterHologramFiles(it))
                }
            } else if (!file.name.startsWith("#") && file.extension.equals("yml", true)) {
                add(file)
            }
            this
        }
    }


}