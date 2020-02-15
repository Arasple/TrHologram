package me.arasple.mc.trhologram.hologram;

import com.google.common.collect.Lists;
import io.izzel.taboolib.module.inject.TSchedule;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.util.Files;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.action.TrAction;
import me.arasple.mc.trhologram.api.TrHologramAPI;
import me.arasple.mc.trhologram.utils.FileWatcher;
import me.arasple.mc.trhologram.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
            if (!key.matches("(?i)viewDistance|viewCondition|location|contents|actions")) {
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
        loadHolograms(Bukkit.getConsoleSender());
    }

    public static void loadHolograms(CommandSender sender) {
        try {
            long start = System.currentTimeMillis();
            List<File> hologramFiles = grabFiles(FOLDER);
            holograms.removeIf(hologram -> {
                if (hologramFiles.stream().noneMatch(f -> f.getName().replaceAll("(?i).yml", "").equals(hologram.getName()))) {
                    Files.deepDelete(new File(hologram.getLoadedFrom()));
                    hologram.destroyAll();
                    return true;
                }
                return false;
            });
            for (File file : hologramFiles) {
                String name = file.getName().replaceAll("(?i).yml", "");
                YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
                Hologram hologram = TrHologramAPI.getHologramById(name);
                if (hologram == null) {
                    hologram = new Hologram(name, Locations.from(data.getString("location")), data.getStringList("contents"), TrAction.readActionGroups(data.get("actions")), data.getString("viewCondition", "100"), data.getString("viewDistance"));
                    getHolograms().add(hologram);
                } else {
                    hologram.applySection(data);
                }
                hologram.update();
                hologram.setLoadedFrom(file.getPath());
            }
            if (sender != null && !holograms.isEmpty()) {
                TLocale.sendTo(sender, "HOLOGRAM.LOADED-SUCCESS", holograms.size(), (System.currentTimeMillis() - start));
                getHolograms().forEach(hologram -> {
                    File file = new File(hologram.getLoadedFrom());
                    if (file != null && file.exists()) {
                        FileWatcher.getWatcher().addSimpleListener(file, () -> {
                            if (!writing) {
                                hologram.applySection();
                            } else {
                                TrHologram.LOGGER.warn("Hologram " + hologram.getName() + " is currently saving to file, auto-reload function will not work");
                            }
                        });
                    } else {
                        TrHologram.LOGGER.warn("Hologram " + hologram.getName() + " has empty file, auto-reload function will not work");
                    }
                });
            }
        } catch (Throwable e) {
            if (sender != null) {
                TLocale.sendTo(sender, "HOLOGRAM.LOADED-FAILURE");
            }
            e.printStackTrace();
        }
        getHolograms().forEach(hologram -> Bukkit.getOnlinePlayers().forEach(hologram::display));
        restartTasks();
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

    public static void createHologram(String id, Location location, String... content) {
        Hologram hologram = new Hologram(id, location, Arrays.asList(content), Lists.newArrayList(), "null", "20");
        File file = Files.file(FOLDER.getPath() + "/" + id + ".yml");
        hologram.setLoadedFrom(file.getAbsolutePath());
        holograms.add(hologram);
        Bukkit.getOnlinePlayers().stream().filter(hologram::isVisible).forEach(hologram::display);
        FileWatcher.getWatcher().addSimpleListener(file, () -> {
            if (!writing) {
                hologram.applySection();
            }
        });

        write();
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
        holograms.forEach(HologramManager::write);
        writing = false;
    }

    public static void write(Hologram hologram) {
        File file = new File(hologram.getLoadedFrom());
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        correctData(yaml);
        yaml.set("viewDistance", hologram.getExactViewDistance());
        yaml.set("viewCondition", hologram.getViewCondition());
        yaml.set("location", Locations.write(hologram.getLocation()));
        yaml.set("contents", hologram.getRawContents());
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
