package me.arasple.mc.trhologram.action.acts;

import me.arasple.mc.trhologram.action.base.AbstractAction;
import me.arasple.mc.trhologram.utils.Bungees;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 19:07
 */
public class ActionConnect extends AbstractAction {
    @Override
    public String getName() {
        return "connect|bungee|server";
    }

    @Override
    public void onExecute(Player player) {
        Bungees.connect(player, getContent(player));
    }
}
