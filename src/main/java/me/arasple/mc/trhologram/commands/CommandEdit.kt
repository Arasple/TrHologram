package me.arasple.mc.trhologram.commands

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandTab
import io.izzel.taboolib.module.command.base.CommandType
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.lite.Catchers
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.editor.EditorMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/2/15 12:29
 */
class CommandEdit : BaseSubCommand() {

    override fun getArguments(): Array<Argument> {
        return arrayOf(Argument("Hologram Name", false, CommandTab { TrHologramAPI.getHologramIds() }))
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = sender as Player
        Catchers.getPlayerdata().remove(player.name)
        if (args.isNotEmpty()) {
            editHologram(player, args[0])
            return
        }
        Catchers.call(player, object : Catchers.Catcher {
            override fun before(): Catchers.Catcher {
                TLocale.sendTo(player, "COMMANDS.EDIT.INPUT-NAME")
                return this
            }

            override fun after(input: String): Boolean {
                editHologram(player, input)
                return false
            }

            override fun cancel() {
                TLocale.sendTo(player, "COMMANDS.QUIT")
            }
        })
    }

    private fun editHologram(player: Player, input: String) {
        val hologram = TrHologramAPI.getHologramById(input)
        if (hologram == null) {
            TLocale.sendTo(player, "COMMANDS.EDIT.NOT-EXIST")
        } else {
            TLocale.Display.sendTitle(player, "", "", 5, 10, 5)
            EditorMenu.openEditor(hologram, player)
        }
    }

    override fun getType(): CommandType {
        return CommandType.PLAYER
    }

}