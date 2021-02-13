package me.arasple.mc.trhologram.module.command

import io.izzel.taboolib.module.command.base.*
import me.arasple.mc.trhologram.module.command.impl.*

/**
 * @author Arasple
 * @date 2021/2/11 16:46
 */
@BaseCommand(name = "trhologram", aliases = ["hologram", "trhd"], permission = "trhologram.access")
class CommandHandler : BaseMainCommand() {

    @SubCommand(
        permission = "trhologram.command.create",
        description = "Create a new hologram",
        type = CommandType.PLAYER
    )
    val create: BaseSubCommand = CommandCreate()

    @SubCommand(description = "Delete an existed hologram", type = CommandType.PLAYER)
    val delete: BaseSubCommand = CommandDelete()

    @SubCommand(permission = "trhologram.command.list", description = "List loaded holograms")
    val list: BaseSubCommand = CommandList()

    @SubCommand(permission = "trhologram.command.teleport", description = "Teleport to the hologram's location")
    val teleport: BaseSubCommand = CommandTeleport()

    @SubCommand(permission = "trhologram.command.movehere", description = "Teleport hologram to your location")
    val movehere: BaseSubCommand = CommandMovehere()

    @SubCommand(permission = "trhologram.command.reload", description = "Reload holograms")
    val reload: BaseSubCommand = CommandReload()

    @SubCommand(permission = "trhologram.command.mirror", description = "Monitor performance")
    val mirror: BaseSubCommand = CommandMirror()

}