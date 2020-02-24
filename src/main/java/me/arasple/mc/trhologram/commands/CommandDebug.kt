package me.arasple.mc.trhologram.commands

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.util.Files
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.hologram.Hologram
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scheduler.BukkitWorker
import java.io.InputStreamReader

/**
 * @author Arasple
 * @date 2020/2/13 22:38
 */
class CommandDebug : BaseSubCommand() {

    private val builtTime = YamlConfiguration.loadConfiguration(InputStreamReader(Files.getResource(TrHologram.getPlugin(), "plugin.yml"))).getString("built-time", "Null")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        if (sender is Player) {
            val player = sender
            if (player.hasMetadata("TrHologram-Debug")) {
                player.removeMetadata("TrHologram-Debug", TrHologram.getPlugin())
                sender.sendMessage("§7Canceled...")
            } else {
                player.setMetadata("TrHologram-Debug", FixedMetadataValue(TrHologram.getPlugin(), ""))
                sender.sendMessage("§aEnabled...")
            }
            return
        }

        sender.sendMessage("§3--------------------------------------------------")
        sender.sendMessage("")
        sender.sendMessage("§2Total Holograms: ")
        Hologram.HOLOGRAMS.forEach {
            if (it.value.isNotEmpty()) {
                sender.sendMessage("§r  §8[${it.key}§8] - §7${it.value.size}")
            }
        }
        sender.sendMessage("")
        sender.sendMessage("§2Running Tasks: §6${Bukkit.getScheduler().activeWorkers.stream().filter { t: BukkitWorker -> t.owner === TrHologram.getPlugin() }.count() + Bukkit.getScheduler().pendingTasks.stream().filter { t: BukkitTask -> t.owner === TrHologram.getPlugin() }.count()}")
        sender.sendMessage("§2TabooLib: §f${Plugin.getVersion()}")
        sender.sendMessage("")
        sender.sendMessage("§2TrHologram Built-Time: §b$builtTime")
        sender.sendMessage("")
        sender.sendMessage("§3--------------------------------------------------")
    }

}