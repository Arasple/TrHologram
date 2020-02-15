package me.arasple.mc.trhologram

import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trhologram.hologram.Hologram
import me.arasple.mc.trhologram.hologram.HologramManager
import me.arasple.mc.trhologram.updater.Updater
import me.arasple.mc.trhologram.utils.FileWatcher
import org.bukkit.configuration.file.YamlConfiguration
import java.io.InputStreamReader
import java.util.function.Consumer

/**
 * @author Arasple
 * @date 2020/2/14 9:28
 */
class TrHologramLoader {

    fun init() {
        TLocale.sendToConsole("PLUGIN.LOADING")
        Updater.init(TrHologram.getPlugin())
        updateConfig()
    }

    fun active() {
        TLocale.sendToConsole("PLUGIN.ENABLED", TrHologram.getPlugin().description.version)
    }

    fun cancel() {
        HologramManager.getHolograms().forEach(Consumer { obj: Hologram -> obj.destroyAll() })
        FileWatcher.getWatcher().unregisterAll()
        HologramManager.write()
        TLocale.sendToConsole("PLUGIN.DISABLED")
    }

    private fun updateConfig() {
        val oldCfg: TConfig = TrHologram.SETTINGS
        val inputStream = TrHologram.getPlugin().getResource("settings.yml")
        if (inputStream == null) {
            TrHologram.LOGGER.error("Can not find resource settings.yml in plugin")
            return
        }
        val newCfg = YamlConfiguration.loadConfiguration(InputStreamReader(inputStream))
        if (newCfg.getInt("CONFIG-VERSION") == TrHologram.SETTINGS.getInt("CONFIG-VERSION")) {
            return
        }
        oldCfg["CONFIG-VERSION"] = newCfg.getInt("CONFIG-VERSION")
        oldCfg.getKeys(true).forEach(Consumer { key: String? ->
            if (!newCfg.contains(key!!)) {
                oldCfg[key] = null
            }
        })
        newCfg.getKeys(true).forEach(Consumer { key: String? ->
            if (!oldCfg.contains(key!!)) {
                oldCfg[key] = newCfg[key]
            }
        })
        oldCfg.saveToFile()
    }

}