package me.arasple.mc.trhologram.hologram;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.gson.Gson;
import io.izzel.taboolib.module.config.TConfigWatcher;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Files;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.action.TrAction;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Arasple
 * @date 2020/1/30 11:13
 */
public class HologramManager {

    private static final File FOLDER = new File(TrHologram.getPlugin().getDataFolder(), "holograms");
    private static List<Hologram> holograms = Lists.newArrayList();
    private static boolean writing;

    private static void correctData(YamlConfiguration hologramData) {
        hologramData.getValues(false).keySet().forEach(key -> {
            if (!key.matches("(?i)viewDistance|viewCondition|update|location|contents|actions")) {
                hologramData.set(key, null);
            }
        });

    }

    public static List<Hologram> getHolograms() {
        return holograms;
    }

    public static File getFolder() {
        return FOLDER;
    }

    @TSchedule
    private static void loadHolograms() {
        loadHolograms(null);
    }

    public static void loadHolograms(CommandSender sender) {
        try {
            long start = System.currentTimeMillis();
            Gson gson = new Gson();

            List<File> hologramFiles = grabFiles(FOLDER);
            holograms.removeIf(hologram -> {
                if (hologramFiles.stream().noneMatch(f -> f.getName().equals(hologram.getName()))) {
                    deleteHologram(hologram.getName());
                    return true;
                }
                return false;
            });
            for (File file : hologramFiles) {
                String name = file.getName().replaceAll("(?i).yml", "");
                YamlConfiguration json = YamlConfiguration.loadConfiguration(file);
                Hologram hologram = TrHologramAPI.getHologramById(name);
                if (hologram == null) {
                    hologram = new Hologram(name, Locations.from(json.getString("location")), json.getStringList("contents"), TrAction.readActions(json.getStringList("actions")), json.getString("viewCondition", "100"), json.getString("viewDistance"), json.getInt("update", 100));
                    getHolograms().add(hologram);
                } else {
                    applySection(hologram, json);
                }
                hologram.update();
                hologram.setLoadedFrom(file.getPath());
            }
            if (sender != null) {
                TLocale.sendTo(sender, "HOLOGRAM.LOADED-SUCCESS", holograms.size(), (System.currentTimeMillis() - start));
                getHolograms().forEach(hologram -> {
                    File file = new File(hologram.getLoadedFrom());
                    if (file != null && file.exists()) {
                        TConfigWatcher.getInst().addSimpleListener(file, () -> {
                            if (!writing) {
                                applySection(hologram, YamlConfiguration.loadConfiguration(file));
                            }
                        });
                    }
                });
            }
        } catch (Throwable e) {
            if (sender != null) {
                TLocale.sendTo(sender, "HOLOGRAM.LOADED-FAILURE");
            }
            e.printStackTrace();
        }
        restartTasks();
    }

    private static void applySection(Hologram hologram, YamlConfiguration data) {
        hologram.setLocation(Locations.from(data.getString("location")));
        hologram.setContents(data.getStringList("contents"));
        hologram.setActions(TrAction.readActions(data.getStringList("actions")));
        hologram.setViewCondition(data.getString("viewCondition"));
        hologram.setViewDistance(data.getString("viewDistance"));
        hologram.setUpdate(data.getInt("update", 100));
    }

    private static List<File> grabFiles(File folder) {
        List<File> files = new ArrayList<>();
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                files.addAll(grabFiles(file));
            }
        } else if (folder.getName().toLowerCase().endsWith(".yml") && folder.length() > 0) {
            files.add(folder);
        }
        return files;
    }

    private static void restartTasks() {
        holograms.forEach(Hologram::runTask);
    }

    public static void createHologram(String id, Location location, String content) {
        Hologram hologram = new Hologram(id, location, Collections.singletonList(content), Lists.newArrayList(), "null", "-1", 100);
        File file = Files.file(FOLDER.getPath() + "/" + id + ".yml");
        hologram.setLoadedFrom(file.getPath());
        holograms.add(hologram);
        Bukkit.getOnlinePlayers().stream().filter(hologram::isVisible).forEach(hologram::display);
        write();
        TConfigWatcher.getInst().addSimpleListener(file, () -> {
            if (!writing) {
                applySection(hologram, YamlConfiguration.loadConfiguration(file));
            }
        });
    }

    public static void deleteHologram(String id) {
        holograms.removeIf(hologram -> {
            if (hologram.getName().equalsIgnoreCase(id)) {
                Files.deepDelete(new File(hologram.getLoadedFrom()));
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
            File file = new File(hologram.getLoadedFrom());
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            correctData(yaml);
            yaml.set("viewDistance", hologram.getExactViewDistance());
            yaml.set("viewCondition", hologram.getViewCondition());
            yaml.set("update", hologram.getUpdate());
            yaml.set("location", Locations.write(hologram.getLocation()));
            yaml.set("contents", hologram.getContentsAsList());
            yaml.set("actions", Lists.newArrayList());
            try {
                yaml.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writing = false;
    }

}
