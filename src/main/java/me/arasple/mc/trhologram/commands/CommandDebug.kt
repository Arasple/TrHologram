package me.arasple.mc.trhologram.commands

import io.izzel.taboolib.module.command.base.BaseCommand
import io.izzel.taboolib.module.command.base.BaseMainCommand
import me.arasple.mc.trhologram.hologram.HologramContent
import me.arasple.mc.trhologram.hologram.HologramManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.function.Consumer

/**
 * @author Arasple
 * @date 2020/2/14 8:56
 */
@BaseCommand(name = "holyshit")
class CommandDebug : BaseMainCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            println("HoloSize: " + HologramManager.getHolograms().size)
            println("Holograms: \n")
            HologramManager.getHolograms().forEach(Consumer { hologram ->
                println("---: " + hologram.viewers)
                hologram.contents.forEach(Consumer { hologramContent: HologramContent -> println("---: " + hologramContent.text) })
            })
        } else {
            HologramManager.getHolograms().clear()
        }
        return true
    }
}
