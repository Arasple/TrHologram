package me.arasple.mc.trhologram.module.command.impl

import io.izzel.taboolib.module.command.base.BaseSubCommand
import me.arasple.mc.trhologram.module.service.Performance
import me.arasple.mc.trhologram.util.Tasks
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author Arasple
 * @date 2021/2/12 18:45
 */
class CommandMirror : BaseSubCommand() {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        Tasks.task(true) {
            Performance.collect {
                childFormat = "§8  {0}§7{1} §2[{3} ms] §7{4}%"
                parentFormat = "§8  §8{0}§7{1} §8[{3} ms] §7{4}%"
            }.run {
                sender.sendMessage("\n§2§lHologram §a§l§nPerformance Mirror\n§r")
                print(sender, getTotal(), 0)
            }
        }
    }

}