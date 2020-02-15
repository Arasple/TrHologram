package me.arasple.mc.trhologram.commands

import io.izzel.taboolib.module.command.base.Argument
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.util.lite.Catchers
import io.izzel.taboolib.util.lite.Catchers.Catcher
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.hologram.HologramManager
import me.arasple.mc.trhologram.utils.Locations
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/2/13 22:38
 */
class CommandCreate : BaseSubCommand() {

    override fun getArguments(): Array<Argument> {
        return arrayOf(Argument("New Hologram Name", false))
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = sender as Player
        Catchers.getPlayerdata().remove(player.name)
        if (args.isNotEmpty()) {
            createHologram(player, args[0])
            return
        }
        Catchers.call(player, object : Catcher {
            override fun before(): Catcher {
                TLocale.sendTo(player, "COMMANDS.CREATE.INPUT-NAME")
                return this
            }

            override fun after(input: String): Boolean {
                createHologram(player, input)
                return false
            }

            override fun cancel() {
                TLocale.sendTo(player, "COMMANDS.QUIT")
            }
        })
    }

    private fun createHologram(player: Player, input: String) {
        if (TrHologramAPI.getHologramById(input) != null) {
            TLocale.sendTo(player, "COMMANDS.CREATE.EXISTED")
        } else {
            HologramManager.createHologram(input, Locations.getLocationForHologram(player), "&3Created a new hologram named &a$input")
            TLocale.sendTo(player, "COMMANDS.CREATE.SUCCESS")
        }
    }

    override fun getType(): CommandType {
        return CommandType.PLAYER
    }

}