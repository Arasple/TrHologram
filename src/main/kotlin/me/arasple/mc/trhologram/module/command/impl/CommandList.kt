package me.arasple.mc.trhologram.module.command.impl

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2021/2/12 17:59
 */
class CommandList : BaseSubCommand() {

    override fun getArguments() = arrayOf(
        Argument("Filter", false)
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>) {
        val filter = if (args.isNotEmpty()) args.joinToString(" ") else null
        val holograms = Hologram.holograms.filter { filter == null || it.id.contains(filter, true) }.sortedBy { it.id }

        if (holograms.isEmpty()) {
            TLocale.sendTo(sender, "Command.List.Error", filter ?: "*")
        } else {
            TLocale.sendTo(sender, "Command.List.Header", holograms.size, filter ?: "*")

            holograms.forEach {
                TLocale.sendTo(
                    sender,
                    "Command.List.Format",
                    it.id,
                    it.components.size
                )
            }
        }
    }

}