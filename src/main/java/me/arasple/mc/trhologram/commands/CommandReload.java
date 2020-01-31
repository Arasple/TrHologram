package me.arasple.mc.trhologram.commands;

import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import me.arasple.mc.trhologram.hologram.Hologram;
import me.arasple.mc.trhologram.hologram.HologramManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 * @date 2020/1/30 11:25
 */
public class CommandReload extends BaseSubCommand {

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        HologramManager.getHolograms().forEach(Hologram::destroyAll);
        HologramManager.getHolograms().clear();
        HologramManager.loadHolograms(sender);
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
