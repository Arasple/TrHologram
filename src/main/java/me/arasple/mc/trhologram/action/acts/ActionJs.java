package me.arasple.mc.trhologram.action.acts;

import me.arasple.mc.trhologram.action.base.AbstractAction;
import me.arasple.mc.trhologram.utils.JavaScript;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2019/12/28 18:49
 */
public class ActionJs extends AbstractAction {

    @Override
    public String getName() {
        return "js|javascript";
    }

    @Override
    public void onExecute(Player player) {
        JavaScript.run(player, getContent());
    }

}
