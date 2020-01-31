package me.arasple.mc.trhologram.utils;

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author Arasple
 * @date 2020/1/30 13:15
 */
public class Locations {

    public static String write(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }

    public static Location from(String loc) {
        String[] location = loc.split(",");
        return new Location(Bukkit.getWorld(location[0]), NumberUtils.toDouble(location[1]), NumberUtils.toDouble(location[2]), NumberUtils.toDouble(location[3]));
    }

    public static boolean equals(Location loc1, Location loc2) {
        return loc1 != null && loc2 != null && write(loc1).equalsIgnoreCase(write(loc2));
    }
}
