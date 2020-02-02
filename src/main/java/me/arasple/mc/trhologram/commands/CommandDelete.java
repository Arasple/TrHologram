package me.arasple.mc.trhologram.commands;

import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.lite.Catchers;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.hologram.HologramManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/30 11:53
 */
public class CommandDelete extends BaseSubCommand {

    @Override
    public Argument[] getArguments() {
        return new Argument[]{new Argument("Hologram Name", false, TrHologramAPI::getHologramIds)};
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Catchers.getPlayerdata().remove(player.getName());
        if (args.length >= 1) {
            deleteHologram(player, args[0]);
            return;
        }
        Catchers.call(player, new Catchers.Catcher() {
            @Override
            public Catchers.Catcher before() {
                TLocale.sendTo(player, "COMMANDS.DELETE.INPUT-NAME");
                return this;
            }

            @Override
            public boolean after(String input) {
                deleteHologram(player, input);
                return false;
            }

            @Override
            public void cancel() {
                TLocale.sendTo(player, "COMMANDS.QUIT");
            }
        });
    }

    private void deleteHologram(Player player, String input) {
        if (TrHologramAPI.getHologramById(input) == null) {
            TLocale.sendTo(player, "COMMANDS.DELETE.NOT-EXIST");
        } else {
            HologramManager.deleteHologram(input);
            TLocale.sendTo(player, "COMMANDS.DELETE.SUCCESS");
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }
}
