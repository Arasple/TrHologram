package me.arasple.mc.trhologram.hologram

import io.izzel.taboolib.module.inject.TSchedule
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.Files
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.action.TrAction
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.utils.FileWatcher
import me.arasple.mc.trhologram.utils.Locations
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.*
import java.util.function.Consumer

/**
 * @author Arasple
 * @date 2020/2/20 16:57
 */
object HologramManager {

    private val FOLDER = File(TrHologram.getPlugin().dataFolder, "holograms")
    private var writing = false

    @TSchedule
    private fun loadHolograms() {
        loadHolograms(Bukkit.getConsoleSender())
    }

    fun loadHolograms(sender: CommandSender?) {
        try {
            val start = System.currentTimeMillis()
            val hologramFiles = grabFiles(FOLDER)
            Hologram.getHolograms().removeIf { hologram ->
                if (hologramFiles.stream().noneMatch { f: File -> f.name.replace("(?i).yml".toRegex(), "") == hologram.id }) {
                    if (hologram.loadedFrom != null) {
                        Files.deepDelete(File(hologram.loadedFrom))
                    }
                    hologram.destroyAll()
                    return@removeIf true
                }
                false
            }
            for (file in hologramFiles) {
                val name = file.name.replace("(?i).yml".toRegex(), "")
                val data = YamlConfiguration.loadConfiguration(file)
                var hologram = TrHologramAPI.getHologramById(name)
                if (hologram == null) {
                    hologram = Hologram.createHologram(TrHologram.getPlugin(), name, Locations.from(data.getString("location")), data.getStringList("contents"), TrAction.readActionGroups(data["actions"]), data.getString("viewCondition", "100"), data.getString("viewDistance"))
                } else {
                    hologram.initFromSection(data)
                }
                hologram.loadedFrom = file.path
            }
            if (sender != null && !Hologram.getHolograms().isEmpty()) {
                TLocale.sendTo(sender, "HOLOGRAM.LOADED-SUCCESS", Hologram.getHolograms().size, System.currentTimeMillis() - start)
                Hologram.getHolograms().forEach { hologram ->
                    if (hologram.loadedFrom != null) {
                        val file = File(hologram.loadedFrom)
                        if (file.exists() && !FileWatcher.watcher.hasListener(file)) {
                            FileWatcher.watcher.addSimpleListener(file) {
                                if (!writing) {
                                    try {
                                        hologram.initFromSection()
                                        TLocale.sendToConsole("HOLOGRAM.AUTO-RELOADED", hologram.id)
                                    } catch (e: Throwable) {
                                        TLocale.sendToConsole("HOLOGRAM.AUTO-RELOAD-FAILED", hologram.id, Arrays.toString(e.stackTrace))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            if (sender != null) {
                TLocale.sendTo(sender, "HOLOGRAM.LOADED-FAILURE")
            }
            e.printStackTrace()
        }
        Hologram.getHolograms().forEach { hologram -> Bukkit.getOnlinePlayers().forEach { player -> hologram.display(player) } }
        restartTasks()
    }


    fun createHologram(id: String, loc: Location, vararg content: String) {
        val file = Files.file(FOLDER.path + "/" + id + ".yml")
        val holo = Hologram.createHologram(TrHologram.getPlugin(), file, id, loc, content.toList(), mutableListOf(), "null", "30")
        Bukkit.getOnlinePlayers().stream().filter { player -> holo.isVisible(player) }.forEach { player -> holo.display(player) }
        if (!FileWatcher.watcher.hasListener(file)) {
            FileWatcher.watcher.addSimpleListener(file) {
                if (!writing) {
                    try {
                        holo.initFromSection()
                        TLocale.sendToConsole("HOLOGRAM.AUTO-RELOADED", holo.id)
                    } catch (e: Throwable) {
                        TLocale.sendToConsole("HOLOGRAM.AUTO-RELOAD-FAILED", holo.id, Arrays.toString(e.stackTrace))
                    }
                }
            }
        }
        write()
    }

    fun deleteHologram(id: String) {
        val hologram = Hologram.getHolograms().firstOrNull { it.id.equals(id, ignoreCase = true) }
        if (hologram != null) {
            hologram.delete()
            if (hologram.loadedFrom != null) {
                Files.deepDelete(File(hologram.loadedFrom))
            }
        }
        write()
    }

    @TSchedule(delay = 20 * 30, period = 20 * 3 * 60)
    fun write() {
        writing = true
        for (hologram in Hologram.getHolograms()) {
            write(hologram, true)
        }
        Bukkit.getScheduler().runTaskLater(TrHologram.getPlugin(), Runnable { writing = false }, 20)
    }

    fun forceWrite() {
        for (hologram in Hologram.getHolograms()) {
            write(hologram, true)
        }
    }

    fun write(hologram: Hologram, force: Boolean) {
        if (!force) {
            writing = true
        }
        val loadedFrom = hologram.loadedFrom ?: return
        val file = File(loadedFrom)
        val yaml = YamlConfiguration.loadConfiguration(file)
        correctData(yaml)
        yaml["viewDistance"] = hologram.viewDistance
        yaml["viewCondition"] = hologram.viewCondition
        yaml["location"] = Locations.write(hologram.loc)
        yaml["contents"] = hologram.contents
        try {
            yaml.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (!force) {
            Bukkit.getScheduler().runTaskLater(TrHologram.getPlugin(), Runnable { writing = false }, 20)
        }
    }

    private fun restartTasks() {
        Hologram.getHolograms().forEach { holo -> holo.lines.forEach { line -> line.runTask() } }
    }

    private fun grabFiles(folder: File): List<File> {
        val files: MutableList<File> = ArrayList()
        if (folder.isDirectory) {
            for (file in folder.listFiles()) {
                files.addAll(grabFiles(file))
            }
        } else if (folder.name.toLowerCase().endsWith(".yml") && folder.length() > 0) {
            files.add(folder)
        }
        return files
    }

    private fun correctData(data: YamlConfiguration) {
        data.getValues(false).keys.forEach(Consumer { key: String ->
            if (!key.matches(Regex("(?i)viewDistance|viewCondition|location|contents|actions"))) {
                data[key] = null
            }
        })
    }

}