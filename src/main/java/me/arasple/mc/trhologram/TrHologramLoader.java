package me.arasple.mc.trhologram;

import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trhologram.hologram.HologramManager;
import me.arasple.mc.trhologram.updater.Updater;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;

/**
 * @author Arasple
 * @date 2020/1/1 16:57
 */
public class TrHologramLoader {

    static void unload() {
        HologramManager.write();
        TLocale.sendToConsole("PLUGIN.DISABLED");
    }

    void init() {
        TLocale.sendToConsole("PLUGIN.LOADING");
        updateConfig();
        Updater.init(TrHologram.getPlugin());
    }

    void load() {
        TLocale.sendToConsole("PLUGIN.ENABLED", TrHologram.getPlugin().getDescription().getVersion());
    }

    private void updateConfig() {
        TConfig oldCfg = TrHologram.getSettings();
        YamlConfiguration newCfg = YamlConfiguration.loadConfiguration(new InputStreamReader(TrHologram.getPlugin().getResource("settings.yml")));
        if (newCfg.getInt("CONFIG-VERSION") == TrHologram.getSettings().getInt("CONFIG-VERSION")) {
            return;
        }
        oldCfg.set("CONFIG-VERSION", newCfg.getInt("CONFIG-VERSION"));
        oldCfg.getKeys(true).forEach(key -> {
            if (!newCfg.contains(key)) {
                oldCfg.set(key, null);
            }
        });
        newCfg.getKeys(true).forEach(key -> {
            if (!oldCfg.contains(key)) {
                oldCfg.set(key, newCfg.get(key));
            }
        });
        oldCfg.saveToFile();
    }

}
