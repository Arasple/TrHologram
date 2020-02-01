package me.arasple.mc.trhologram.action.acts;

import io.izzel.taboolib.util.Commands;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.action.base.AbstractAction;
import me.arasple.mc.trhologram.utils.Vars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 18:51
 */
public class ActionCommand extends AbstractAction {

    @Override
    public String getName() {
        return "player|command|execute";
    }

    @Override
    public void onExecute(Player player) {
        Bukkit.getScheduler().runTask(TrHologram.getPlugin(), () -> Commands.dispatchCommand(player, Vars.replace(player, getContent())));
    }

}
