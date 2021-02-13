package me.arasple.mc.trhologram.module.conf

import io.izzel.taboolib.module.locale.TLocale
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
import me.arasple.mc.trhologram.module.display.texture.TrMenuTexture
import me.arasple.mc.trhologram.module.hook.HookPlugin
import me.arasple.mc.trhologram.util.parseLocation
import me.arasple.mc.trhologram.util.parseString
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.system.measureNanoTime

/**
 * @author Arasple
 * @date 2021/2/11 10:03
 */
object HologramLoader {

    private val folder = Files.folder(TrHologram.plugin.dataFolder, "holograms")

    fun create(id: String, location: Location): Hologram {
        val hologram =
            """
            Location: ${location.parseString()}

            Contents:
              - '&7Hello, &2Tr&aHologram&7!'
              - '{item: emerald}'
              - '&3Nice to meet you, &a{{player name}}'
              - '&3Author: &aArasple{offset=0.35}'

            Actions:
              All: 'tell color *"&bHi, you just clicked this hologram"'
            """.trimIndent()

        Files.file(folder, "$id.yml").also {
            it.writeText(hologram, StandardCharsets.UTF_8)
            return load(it)
        }
    }

    fun load(sender: CommandSender) {
        measureNanoTime { load() }.div(1000000.0).let {
            TLocale.sendTo(sender, "Hologram.Loaded", Hologram.holograms.size, it)
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
        val location = conf.getString("Location")?.parseLocation() ?: throw Exception("No valid location")
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

        val all = conf.get("Actions.All").toStringList()
        val left = conf.get("Actions.Left").toStringList()
        val shiftLeft = conf.get("Actions.Shift_Left").toStringList()
        val right = conf.get("Actions.Right").toStringList()
        val shiftRight = conf.get("Actions.Shift_Right").toStringList()

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
                isItem ->{
                    val texture = if (!HookPlugin.TRMENU) Texture.createTexture(itemDisplay!!) else TrMenuTexture(itemDisplay!!)
                    ItemHologram(texture, position, update)
                }
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
            ClickReaction(actions),
            file.absolutePath
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

    @Suppress("UNCHECKED_CAST")
    private fun Any?.toStringList(): List<String> {
        return when (this) {
            is List<*> -> this as List<String>
            else -> listOf(toString())
        }
    }

}