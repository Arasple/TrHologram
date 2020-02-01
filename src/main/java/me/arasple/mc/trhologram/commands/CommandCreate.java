package me.arasple.mc.trhologram.commands;

import io.izzel.taboolib.module.command.base.Argument;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.command.base.CommandType;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.lite.Catchers;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.hologram.HologramManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/30 11:53
 */
public class CommandCreate extends BaseSubCommand {

    @Override
    public Argument[] getArguments() {
        return new Argument[]{new Argument("New Hologram Name", false)};
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length >= 1) {
            createHologram(player, args[0]);
            return;
        }
        Catchers.getPlayerdata().remove(player.getName());
        Catchers.call(player, new Catchers.Catcher() {
            @Override
            public Catchers.Catcher before() {
                TLocale.sendTo(player, "COMMANDS.CREATE.INPUT-NAME");
                return this;
            }

            @Override
            public boolean after(String input) {
                createHologram(player, input);
                return false;
            }

            @Override
            public void cancel() {
                TLocale.sendTo(player, "COMMANDS.QUIT");
            }
        });
    }

    private void createHologram(Player player, String input) {
        if (TrHologramAPI.getHologramById(input) != null) {
            TLocale.sendTo(player, "COMMANDS.CREATE.EXISTED");
        } else {
            HologramManager.createHologram(input, new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()), "&3Created a new hologram named &a" + input);
            TLocale.sendTo(player, "COMMANDS.CREATE.SUCCESS");
        }
    }

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }
}
