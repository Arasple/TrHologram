package me.arasple.mc.trhologram.commands;

import io.izzel.taboolib.module.command.base.BaseCommand;
import io.izzel.taboolib.module.command.base.BaseMainCommand;
import me.arasple.mc.trhologram.hologram.HologramManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/29 21:11
 */
@BaseCommand(name = "holyshit")
public class CommandDebug extends BaseMainCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            System.out.println("HoloSize: " + HologramManager.getHolograms().size());
            System.out.println("Holograms: \n");
            HologramManager.getHolograms().forEach(hologram -> {
                System.out.println("---: " + hologram.getViewers());

                hologram.getContents().forEach(hologramContent -> {
                    System.out.println("---: " + hologramContent.getText());
                });
            });
        } else {
            HologramManager.getHolograms().clear();
        }
        return true;
    }

}
