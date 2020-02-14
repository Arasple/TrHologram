package me.arasple.mc.trhologram.commands

import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.module.tellraw.TellrawJson
import io.izzel.taboolib.util.Strings
import me.arasple.mc.trhologram.hologram.HologramManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.function.Consumer

/**
 * @author Arasple
 * @date 2020/2/14 9:05
 */
class CommandList : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        TLocale.sendTo(sender, "COMMANDS.LIST")
        HologramManager.getHolograms().forEach(Consumer { hologram -> TellrawJson.create().append(Strings.replaceWithOrder(TLocale.asString("COMMANDS.LIST-FORMAT"), hologram.name)).hoverText("§7点击立即传送!").clickCommand("/minecraft:tp " + sender.name + " " + hologram.location.x + " " + (hologram.location.y + 1) + " " + hologram.location.z).send(sender) })
        sender.sendMessage("")
    }

    override fun getType(): CommandType {
        return CommandType.ALL
    }

}