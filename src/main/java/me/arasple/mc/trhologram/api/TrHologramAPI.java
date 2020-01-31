package me.arasple.mc.trhologram.api;

import me.arasple.mc.trhologram.hologram.Hologram;
import me.arasple.mc.trhologram.hologram.HologramContent;
import me.arasple.mc.trhologram.hologram.HologramManager;

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


}
