package me.arasple.mc.trhologram.action.acts;

import io.izzel.taboolib.util.Commands;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.action.base.AbstractAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/22 10:22
 */
public class ActionCommandConsole extends AbstractAction {

    @Override
    public String getName() {
        return "console";
    }

    @Override
    public void onExecute(Player player) {
        Bukkit.getScheduler().runTask(TrHologram.getPlugin(), () -> Commands.dispatchCommand(Bukkit.getConsoleSender(), getContent(player)));
    }

}
