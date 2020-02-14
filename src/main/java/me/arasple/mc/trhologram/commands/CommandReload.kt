package me.arasple.mc.trhologram.commands

import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import me.arasple.mc.trhologram.hologram.HologramManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.function.Consumer

/**
 * @author Arasple
 * @date 2020/2/14 9:07
 */
class CommandReload : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        HologramManager.getHolograms().forEach(Consumer { hologram -> hologram.destroyAll() })
        HologramManager.getHolograms().clear()
        HologramManager.loadHolograms(sender)
    }

    override fun getType(): CommandType {
        return CommandType.ALL
    }
}
