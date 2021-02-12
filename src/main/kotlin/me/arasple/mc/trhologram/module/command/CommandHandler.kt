package me.arasple.mc.trhologram.module.command

import io.izzel.taboolib.module.command.base.*
import me.arasple.mc.trhologram.module.command.impl.CommandCreate

/**
 * @author Arasple
 * @date 2021/2/11 16:46
 */
@BaseCommand(name = "trhologram", aliases = ["hologram"], permission = "trhologram.access")
class CommandHandler : BaseMainCommand() {

    @SubCommand(permission = "trhologram.command.create", type = CommandType.PLAYER)
    val create: BaseSubCommand = CommandCreate()

}