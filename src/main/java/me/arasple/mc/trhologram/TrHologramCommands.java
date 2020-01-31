package me.arasple.mc.trhologram;

import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.SubCommand;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trhologram.commands.CommandCreate;
import me.arasple.mc.trhologram.commands.CommandDelete;
import me.arasple.mc.trhologram.commands.CommandList;
import me.arasple.mc.trhologram.commands.CommandReload;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 * @date 2020/1/30 11:25
 */
@BaseCommand(name = "trhologram", aliases = {"hd", "holo", "tholo"}, permission = "trhologram.admin")
public class TrHologramCommands extends BaseMainCommand {

    @SubCommand(description = "Create hologram")
    BaseSubCommand create = new CommandCreate();

    @SubCommand(description = "Delete hologram")
    BaseSubCommand delete = new CommandDelete();

    @SubCommand(description = "Reload holograms")
    BaseSubCommand reload = new CommandReload();

    @SubCommand(description = "List holograms")
    BaseSubCommand list = new CommandList();

    @Override
    public String getCommandTitle() {
        return "§2--------------------------------------------------";
    }

    @Override
    public void onCommandHelp(CommandSender sender, Command command, String label, String[] args) {
        TLocale.sendTo(sender, "COMMANDS.HELP-PAGE", TrHologram.getPlugin().getDescription().getVersion(), label.toUpperCase().charAt(0) + label.substring(1));
    }

}
