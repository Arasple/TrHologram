package me.arasple.mc.trhologram

import io.izzel.taboolib.module.command.base.BaseCommand
import io.izzel.taboolib.module.command.base.BaseMainCommand
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.SubCommand
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trhologram.commands.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2020/2/14 9:31
 */
@BaseCommand(name = "trhologram", aliases = ["tholo", "trhd", "hd", "holo", "hologram"], permission = "trhologram.admin")
class TrHologramCommands : BaseMainCommand() {

    @SubCommand(description = "Create hologram")
    var create: BaseSubCommand = CommandCreate()
    @SubCommand(description = "Delete hologram")
    var delete: BaseSubCommand = CommandDelete()
    @SubCommand(description = "Delete hologram")
    var edit: BaseSubCommand = CommandEdit()
    @SubCommand(description = "Reload holograms")
    var reload: BaseSubCommand = CommandReload()
    @SubCommand(description = "List holograms")
    var list: BaseSubCommand = CommandList()
    @SubCommand(description = "Debug hologram")
    var debug: BaseSubCommand = CommandDebug()

    override fun getCommandTitle(): String {
        return "§2--------------------------------------------------"
    }

    override fun onCommandHelp(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        TLocale.sendTo(sender, "COMMANDS.HELP-PAGE", TrHologram.getPlugin().description.version, label.toUpperCase()[0].toString() + label.substring(1))
    }

}
