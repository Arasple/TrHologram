package me.arasple.mc.trhologram.migrate;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Commands;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.hologram.HologramManager;
import me.arasple.mc.trhologram.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Arasple
 * @date 2020/1/30 11:19
 * 迁移你妈啦个逼
 */
public class HolographicDisplaysMigrater {

    @TSchedule
    private static void migrate() throws IOException {
        File file = new File(TrHologram.getPlugin().getDataFolder().getParentFile(), "HolographicDisplays/database.yml");
        if (file.exists()) {
            YamlConfiguration database = YamlConfiguration.loadConfiguration(file);
            List<String> migrated = Lists.newArrayList();
            database.getKeys(false).forEach(id -> {
                ConfigurationSection hologram = database.getConfigurationSection(id);
                if (hologram.contains("location") && hologram.contains("lines") && !hologram.getStringList("lines").isEmpty()) {
                    migrated.add(id);
                    HologramManager.createHologram(id, Locations.from(hologram.getString("location")).subtract(0, 1.2, 0), hologram.getStringList("lines").toArray(new String[0]));
                    database.set(id, null);
                }
            });
            if (migrated.size() > 0) {
                TLocale.sendToConsole("MIGRATED", migrated.size());
                Commands.dispatchCommand(Bukkit.getConsoleSender(), "holographicdisplays reload");
                database.save(file);
            }
        }
    }

}
