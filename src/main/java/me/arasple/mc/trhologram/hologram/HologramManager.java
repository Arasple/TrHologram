package me.arasple.mc.trhologram.hologram;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trhologram.action.TrAction;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Arasple
 * @date 2020/1/30 11:13
 */
public class HologramManager {

    @TInject("holograms.yml")
    private static TConfig hologramsData;
    private static List<Hologram> holograms = Lists.newArrayList();
    private static boolean writing;

    @TSchedule
    static void setup() {
        hologramsData.listener(() -> {
            if (!writing) {
                correctData();
                loadHolograms();
            }
        });
    }

    private static void correctData() {
        hologramsData.getValues(false).keySet().forEach(key -> {
            if (!hologramsData.contains(key + ".contents")) {
                hologramsData.set(key, null);
            }
        });
    }

    public static List<Hologram> getHolograms() {
        return holograms;
    }

    public static TConfig getHologramsData() {
        return hologramsData;
    }

    @TSchedule
    private static void loadHolograms() {
        loadHolograms(null);
    }

    public static void loadHolograms(CommandSender sender) {
        try {
            long start = System.currentTimeMillis();
            Set<String> hologramNames = hologramsData.getValues(false).keySet();
            holograms.removeIf(hologram -> {
                if (!hologramNames.contains(hologram.getName())) {
                    deleteHologram(hologram.getName());
                    return true;
                }
                return false;
            });
            hologramNames.forEach(name -> {
                ConfigurationSection section = hologramsData.getConfigurationSection(name);
                Hologram hologram = TrHologramAPI.getHologramById(name);
                if (hologram == null) {
                    hologram = new Hologram(name, Locations.from(section.getString("location")), section.getStringList("contents"), TrAction.readActions(section.getStringList("actions")), section.getString("viewCondition"), section.getString("viewDistance", "-1"), section.getInt("update", 100));
                    getHolograms().add(hologram);
                } else {
                    hologram.setLocation(Locations.from(section.getString("location")));
                    hologram.setContents(section.getStringList("contents"));
                    hologram.setActions(TrAction.readActions(section.getStringList("actions")));
                    hologram.setViewCondition(section.getString("viewCondition"));
                    hologram.setViewDistance(section.getString("viewDistance"));
                    hologram.setUpdate(section.getInt("update", 100));
                }
                hologram.update();
            });
            if (sender != null) {
                TLocale.sendTo(sender, "HOLOGRAM.LOADED-SUCCESS", holograms.size(), (System.currentTimeMillis() - start));
            }
        } catch (Throwable e) {
            if (sender != null) {
                TLocale.sendTo(sender, "HOLOGRAM.LOADED-FAILURE");
            }
            e.printStackTrace();
        }
        restartTasks();
    }

    private static void restartTasks() {
        holograms.forEach(Hologram::runTask);
    }

    public static void createHologram(String id, Location location, String content) {
        Hologram hologram = new Hologram(id, location, Collections.singletonList(content), Lists.newArrayList(), "null", "-1", 100);
        holograms.add(hologram);
        Bukkit.getOnlinePlayers().stream().filter(hologram::isVisible).forEach(hologram::display);
        write();
    }

    public static void deleteHologram(String id) {
        holograms.removeIf(hologram -> {
            if (hologram.getName().equalsIgnoreCase(id)) {
                if (hologramsData.contains(hologram.getName())) {
                    hologramsData.set(hologram.getName(), null);
                }
                hologram.destroyAll();
                return true;
            }
            return false;
        });
        write();
    }

    @TSchedule(delay = 20 * 30, period = 20 * 3 * 60)
    public static void write() {
        writing = true;
        holograms.forEach(hologram -> {
            hologramsData.set(hologram.getName() + ".viewDistance", hologram.getExactViewDistance());
            hologramsData.set(hologram.getName() + ".viewCondition", hologram.getViewCondition());
            hologramsData.set(hologram.getName() + ".update", hologram.getUpdate());
            hologramsData.set(hologram.getName() + ".location", Locations.write(hologram.getLocation()));
            hologramsData.set(hologram.getName() + ".contents", hologram.getContentsAsList());
            hologramsData.set(hologram.getName() + ".actions", Lists.newArrayList());
        });
        hologramsData.saveToFile();
        writing = false;
    }

}
