package me.arasple.mc.trhologram.updater

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.izzel.taboolib.module.inject.TSchedule
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.IO
import me.arasple.mc.trhologram.TrHologram
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import java.io.BufferedInputStream
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @author Arasple
 * @date 2020/2/24 15:24
 */
class Updater : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val p = e.player
        if (isOld && !noticed.contains(p.uniqueId) && p.hasPermission("trhologram.admin")) {
            noticed.add(p.uniqueId)
            Bukkit.getScheduler().runTaskLaterAsynchronously(TrHologram.getPlugin(), Runnable { TLocale.sendTo(p, "PLUGIN.UPDATER.OLD", newVersion) }, 1)
        }
    }

    companion object {

        private var isAutoUpdate = false
        private val noticed: MutableList<UUID> = ArrayList()
        private var url: String? = null
        var version = 0.0
        var isOld = false
        var newVersion = 0.0

        fun init(plugin: Plugin) {
            url = "https://api.github.com/repos/Arasple/" + plugin.name + "/releases/latest"
            version = TrHologram.getTrVersion()
            newVersion = version
            setAutoUpdate()
            if (!version.toString().equals(plugin.description.version.split("-").toTypedArray()[0], ignoreCase = true)) {
                TLocale.sendToConsole("ERROR.VERSION")
                Bukkit.shutdown()
            }
            Bukkit.getPluginManager().registerEvents(Updater(), plugin)
        }

        private fun notifyOld() {
            if (newVersion - version >= 0.2) {
                val last = Math.min((1 * ((newVersion - version) / 0.01)).toInt(), 5)
                TLocale.sendToConsole("PLUGIN.UPDATER.TOO-OLD", last)
                try {
                    Thread.sleep(last * 1000.toLong())
                } catch (ignored: InterruptedException) {
                }
            } else {
                if (isOld) {
                    TLocale.sendToConsole("PLUGIN.UPDATER.OLD", newVersion)
                } else {
                    TLocale.sendToConsole("PLUGIN.UPDATER." + if (version > newVersion) "DEV" else "LATEST")
                }
            }
        }

        @TSchedule(delay = 20, period = 10 * 60 * 20, async = true)
        private fun grabInfo() {
            if (isOld) {
                return
            }
            var read: String?
            try {
                URL(url).openStream().use { inputStream ->
                    BufferedInputStream(inputStream).use { bufferedInputStream ->
                        read = IO.readFully(bufferedInputStream, StandardCharsets.UTF_8)
                        val json = JsonParser().parse(read) as JsonObject
                        val latestVersion = json["tag_name"].asDouble
                        if (latestVersion > version) {
                            isOld = true
                            notifyOld()
                        }
                        newVersion = latestVersion
                    }
                }
            } catch (ignored: Exception) {
            }
        }

        private fun setAutoUpdate() {
            isAutoUpdate = TrHologram.SETTINGS.getBoolean("OPTIONS.AUTO-UPDATE", false)
        }

    }

}
