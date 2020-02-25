package me.arasple.mc.trhologram.api;

import com.google.common.collect.Lists;
import me.arasple.mc.trhologram.action.ActionGroups;
import me.arasple.mc.trhologram.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

/**
 * @author Arasple
 * @date 2020/2/25 14:24
 */
public class TrHologramAPI {

    public static Hologram getHologramById(String id) {
        return Hologram.Companion.getHolograms().stream().filter(hologram -> hologram.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public static Hologram getHologramByEntityId(int entityId) {
        return Hologram.Companion.getHolograms().stream().filter(hologram -> hologram.getLines().stream().anyMatch(line -> line.getId() == entityId)).findFirst().orElse(null);
    }

    public static List<String> getHologramIds() {
        List<String> list = Lists.newArrayList();
        Hologram.Companion.getHolograms().forEach(hologram -> list.add(hologram.getId()));
        return list;
    }

    public static Hologram createHologram(Plugin plugin, String id, Location location, String... contents) {
        return createHologram(plugin, id, location, Arrays.asList(contents));
    }

    public static Hologram createHologram(Plugin plugin, String id, Location location, List<String> contents) {
        return createHologram(plugin, id, location, contents, Lists.newArrayList());
    }

    public static Hologram createHologram(Plugin plugin, String id, Location location, List<String> contents, List<ActionGroups> actions) {
        return createHologram(plugin, id, location, contents, actions, null, null);
    }

    public static Hologram createHologram(Plugin plugin, String id, Location location, List<String> contents, List<ActionGroups> actions, String viewCondition, String viewDistance) {
        return Hologram.Companion.createHologram(plugin, id, location, contents, actions, viewCondition, viewDistance);
    }


}
