package me.arasple.mc.trhologram.nms;

import io.izzel.taboolib.Version;
import io.izzel.taboolib.module.lite.SimpleVersionControl;
import me.arasple.mc.trhologram.TrHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Arasple
 * @date 2020/1/29 20:12
 */
public abstract class HoloPackets {

    private static HoloPackets instance;

    static {
        try {
            if (Version.getCurrentVersion() == Version.v1_15) {
                instance = (HoloPackets) SimpleVersionControl.createNMS("me.arasple.mc.trhologram.nms.imp.HoloPacketsImp_v1_15").useCache().translate(TrHologram.getPlugin()).newInstance();
            }
        } catch (Throwable e) {
            TrHologram.LOGGER.error("An error occurred while adapting your server version " + Bukkit.getVersion() + ", please make sure your version is supported. Plugin will not work");
            e.printStackTrace();
        }
    }

    public static HoloPackets getInst() {
        return instance;
    }

    public abstract void spawnArmorStand(Player player, int entityId, UUID uuid, Location location);

    public abstract void destroyArmorStand(Player player, int entityId);

    public abstract void initArmorStandAsHologram(Player player, int entityId);

    public abstract void updateArmorStandDisplayName(Player player, int entityId, String name);

    public abstract void updateArmorStandLocation(Player player, int entityId, Location location);

    public abstract void updateArmorStandEquipmentItem(Player player, int entityId, EquipmentSlot slot, ItemStack itemStack);

    public abstract void sendEntityMetadata(Player player, int entityId, Object... objects);

    public abstract Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra);

    public abstract Object getMetaEntityGravity(boolean gravity);

    public abstract Object getMetaEntitySilenced(boolean silenced);

    public abstract Object getMetaEntityCustomNameVisible(boolean visible);

    public abstract Object getMetaEntityCustomName(String name);

    public abstract Object getMetaArmorStandProperties(boolean isSmall, boolean hasArms, boolean noBasePlate, boolean marker);


}