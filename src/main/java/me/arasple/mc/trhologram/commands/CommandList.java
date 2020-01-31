package me.arasple.mc.trhologram.commands;

import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.Strings;
import me.arasple.mc.trhologram.hologram.HologramManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author Arasple
 * @date 2020/1/30 11:25
 */
public class CommandList extends BaseSubCommand {

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        TLocale.sendTo(sender, "COMMANDS.LIST");
        HologramManager.getHolograms().forEach(hologram -> TellrawJson.create().append(Strings.replaceWithOrder(TLocale.asString("COMMANDS.LIST-FORMAT"), hologram.getName())).hoverText("§7点击立即传送!").clickCommand("/minecraft:tp " + sender.getName() + " " + hologram.getLocation().getX() + " " + hologram.getLocation().getY() + " " + hologram.getLocation().getZ()).send(sender));
        sender.sendMessage("");
    }

    @Override
    public CommandType getType() {
        return CommandType.ALL;
    }

}
