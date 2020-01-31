package me.arasple.mc.trhologram.utils;

import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Scripts;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.script.SimpleBindings;
import java.util.Arrays;

/**
 * @author Arasple
 * @date 2019/10/5 13:57
 */

public class JavaScript {

    private static SimpleBindings bindings = new SimpleBindings();

    static {
        bindings.put("bukkitServer", Bukkit.getServer());
    }

    public static Object run(Player player, String script) {
        script = Vars.replace(player, script);

        if (Strings.isEmpty(script) || "null".equalsIgnoreCase(script)) {
            return true;
        } else if (script.matches("true|false")) {
            return Boolean.parseBoolean(script);
        } else if (script.matches("(?i)no|yes")) {
            return !"no".equalsIgnoreCase(script);
        }

        bindings.put("player", player);

        try {
            return Scripts.compile(script).eval(bindings);
        } catch (Throwable e) {
            TLocale.sendTo(player, "ERROR.JS", script, e.getMessage(), Arrays.toString(e.getStackTrace()));
            TLocale.sendToConsole("ERROR.JS", script, e.getMessage(), Arrays.toString(e.getStackTrace()));
            return false;
        }
    }

    public static SimpleBindings getBindings() {
        return bindings;
    }

}
