package me.arasple.mc.trhologram

import io.izzel.taboolib.Version
import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.loader.PluginBoot
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trhologram.api.Settings
import me.arasple.mc.trhologram.module.conf.HologramLoader
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.Bukkit


/**
 * @author Arasple
 * @date 2021/1/25 12:11
 */
object TrHologram : Plugin() {


    override fun onLoad() {
        TLocale.sendToConsole("Plugin.Loading", Bukkit.getBukkitVersion())
    }

    override fun onEnable() {
        if (Version.isBefore(Version.v1_9)) {
            PluginBoot.setDisabled(true)
            return
        }

        Settings.init()
        HologramLoader.load(Bukkit.getConsoleSender())
        TLocale.sendToConsole("Plugin.Enabled", plugin.description.version)
    }

    override fun onDisable() {
        Hologram.holograms.forEach(Hologram::destroy)
    }

}