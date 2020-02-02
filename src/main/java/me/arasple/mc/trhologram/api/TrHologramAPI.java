package me.arasple.mc.trhologram.api;

import com.google.common.collect.Lists;
import me.arasple.mc.trhologram.hologram.Hologram;
import me.arasple.mc.trhologram.hologram.HologramContent;
import me.arasple.mc.trhologram.hologram.HologramManager;

import java.util.List;

/**
 * @author Arasple
 * @date 2020/1/29 22:20
 */
public class TrHologramAPI {

    public static Hologram getHologramById(String name) {
        return HologramManager.getHolograms().stream().filter(hologram -> hologram.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Hologram getHologramByEntityId(int id) {
        for (Hologram hologram : HologramManager.getHolograms()) {
            for (HologramContent line : hologram.getContents()) {
                if (line.getId() == id) {
                    return hologram;
                }
            }
        }
        return null;
    }

    public static HologramContent getHologramContentByEntityId(int id) {
        for (Hologram hologram : HologramManager.getHolograms()) {
            for (HologramContent line : hologram.getContents()) {
                if (line.getId() == id) {
                    return line;
                }
            }
        }
        return null;
    }

    public static List<String> getHologramIds() {
        List<String> ids = Lists.newArrayList();
        HologramManager.getHolograms().forEach(hologram -> ids.add(hologram.getName()));
        return ids;
    }

}
