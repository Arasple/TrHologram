package me.arasple.mc.trhologram.module.command.impl

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trhologram.module.conf.HologramLoader
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.io.File

/**
 * @author Arasple
 * @date 2021/2/12 17:43
 */
class CommandReload : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("Id", false) {
            Hologram.holograms.map { it.id }
        }
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val hologram = if (args.isNotEmpty()) Hologram.findHologram { it.id.equals(args[0], true) } else null

        if (hologram != null) {
            hologram.destroy()
            Hologram.holograms.remove(hologram)

            hologram.loadedPath?.let {
                HologramLoader.load(File(it))
                TLocale.sendTo(sender, "Command.Reload", hologram.id)
            }
        } else {
            HologramLoader.load(sender)
            Bukkit.getOnlinePlayers().forEach(Hologram::refreshAll)
        }
    }


}