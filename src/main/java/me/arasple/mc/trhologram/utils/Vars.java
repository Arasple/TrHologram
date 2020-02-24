package me.arasple.mc.trhologram.utils;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.locale.TLocale;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Arasple
 * @date 2019/10/6 21:59
 */
public class Vars {

    private static final boolean IS_PLACEHOLDERAPI_INSTALLED = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

    public static String replace(Player player, String string) {
        return string != null ? setPlaceholders(player, string) : null;
    }

    public static List<String> replace(Player player, List<String> strings) {
        List<String> results = Lists.newArrayList();
        strings.forEach(str -> results.add(replace(player, str)));
        return results.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static String setPlaceholders(Player player, String string) {
        return TLocale.Translate.setPlaceholders(player, TLocale.Translate.setColored(string));
    }

    public static String setBracketPlaceholders(Player player, String content) {
        return IS_PLACEHOLDERAPI_INSTALLED ? PlaceholderAPI.setBracketPlaceholders(player, content) : content;
    }

}
