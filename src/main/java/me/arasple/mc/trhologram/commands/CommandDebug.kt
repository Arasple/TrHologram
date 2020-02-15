package me.arasple.mc.trhologram.commands

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import me.arasple.mc.trhologram.api.TrHologramAPI
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2020/2/13 22:38
 */
class CommandDebug : BaseSubCommand() {

    override fun getArguments(): Array<Argument> {
        return arrayOf(Argument("Hologram Name", true))
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val hologram = TrHologramAPI.getHologramById(args[0])
        if (hologram != null) {
            sender.sendMessage("As String: §7$hologram")
        }
    }

}