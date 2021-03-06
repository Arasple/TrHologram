package me.arasple.mc.trhologram.module.service

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.izzel.taboolib.loader.PluginBoot
import io.izzel.taboolib.module.inject.TFunction
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.module.inject.TSchedule
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.IO
import me.arasple.mc.trhologram.TrHologram
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.io.BufferedInputStream
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/28 18:30
 */
object Updater {

    private val API_URL = "https://api.github.com/repos/Arasple/${TrHologram.plugin.name}/releases/latest"
    private val DESCRIPTION = TrHologram.plugin.description
    private var NOTIFY = false
    val CURRENT_VERSION = DESCRIPTION.version.split("-")[0].toDoubleOrNull() ?: -1.0
    var LATEST_VERSION = -1.0
    val NOTIFIED = mutableSetOf<UUID>()

    @TFunction.Init
    fun init() {
        if (CURRENT_VERSION < 0) PluginBoot.setEnableBoot(false)
    }

    @TSchedule(delay = 20, period = 10 * 60 * 20, async = true)
    private fun grabInfo() {
        if (LATEST_VERSION > 0) {
            return
        }
        val read: String
        try {
            URL(API_URL).openStream().use { inputStream ->
                BufferedInputStream(inputStream).use { bufferedInputStream ->
                    read = IO.readFully(bufferedInputStream, StandardCharsets.UTF_8)
                    val json = JsonParser().parse(read) as JsonObject
                    val latestVersion = json.get("tag_name").asDouble
                    if (latestVersion > CURRENT_VERSION) {
                        LATEST_VERSION = latestVersion
                        if (LATEST_VERSION < 0) PluginBoot.setEnableBoot(false)
                        if (!NOTIFY) {
                            NOTIFY = true
                            TLocale.sendToConsole("Plugin.Update", LATEST_VERSION)
                        }
                    }
                }
            }
        } catch (e: Throwable) {

        }
    }

    @TListener
    class Listen : Listener {

        @EventHandler(priority = EventPriority.HIGHEST)
        fun onJoin(e: PlayerJoinEvent) {
            val player = e.player
            if (player.isOp && LATEST_VERSION > CURRENT_VERSION && !NOTIFIED.contains(player.uniqueId)) {
                TLocale.sendTo(player, "Plugin.Update", LATEST_VERSION)
                NOTIFIED.add(player.uniqueId)
            }
        }

    }

}