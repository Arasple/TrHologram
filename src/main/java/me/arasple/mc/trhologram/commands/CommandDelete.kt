package me.arasple.mc.trhologram.commands

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandTab
import io.izzel.taboolib.module.command.base.CommandType
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.lite.Catchers
import io.izzel.taboolib.util.lite.Catchers.Catcher
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.hologram.HologramManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/2/14 8:57
 */
class CommandDelete : BaseSubCommand() {

    override fun getArguments(): Array<Argument> {
        return arrayOf(Argument("Hologram Name", false, CommandTab { TrHologramAPI.getHologramIds() }))
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = sender as Player
        Catchers.getPlayerdata().remove(player.name)
        if (args.isNotEmpty()) {
            deleteHologram(player, args[0])
            return
        }
        Catchers.call(player, object : Catcher {
            override fun before(): Catcher {
                TLocale.sendTo(player, "COMMANDS.DELETE.INPUT-NAME")
                return this
            }

            override fun after(input: String): Boolean {
                deleteHologram(player, input)
                return false
            }

            override fun cancel() {
                TLocale.sendTo(player, "COMMANDS.QUIT")
            }
        })
    }

    private fun deleteHologram(player: Player, input: String?) {
        if (TrHologramAPI.getHologramById(input) == null) {
            TLocale.sendTo(player, "COMMANDS.DELETE.NOT-EXIST")
        } else {
            HologramManager.deleteHologram(input)
            TLocale.sendTo(player, "COMMANDS.DELETE.SUCCESS")
        }
    }

    override fun getType(): CommandType {
        return CommandType.PLAYER
    }


}
