package me.arasple.mc.trhologram.commands

import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import me.arasple.mc.trhologram.hologram.Hologram
import me.arasple.mc.trhologram.hologram.HologramManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2020/2/14 9:07
 */
class CommandReload : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        Hologram.getHolograms().forEach { hologram -> hologram.destroyAll() }
        HologramManager.loadHolograms(sender)
    }

    override fun getType(): CommandType {
        return CommandType.ALL
    }
}
