package me.arasple.mc.trhologram.commands

import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.CommandType
import me.arasple.mc.trhologram.migrate.HolographicDisplaysMigrater
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2020/2/15 21:54
 */
class CommandMigrate : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        HolographicDisplaysMigrater.migrate()
    }

    override fun getType(): CommandType {
        return CommandType.CONSOLE
    }
}