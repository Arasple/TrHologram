package me.arasple.mc.trhologram.nms;

import io.izzel.taboolib.Version;
import io.izzel.taboolib.module.lite.SimpleVersionControl;
import me.arasple.mc.trhologram.TrHologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2020/1/29 20:12
 */
public abstract class HoloPackets {

    private static HoloPackets instance;

    static {
        try {
            instance = (HoloPackets) SimpleVersionControl.createNMS("me.arasple.mc.trhologram.nms.imp.HoloPacketsImp_" + Version.getCurrentVersion().name()).useNMS().translate(TrHologram.getPlugin()).newInstance();
        } catch (InstantiationException | IllegalAccessException | IOException e) {
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

    public abstract Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean unused, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra);

    public abstract Object getMetaEntityGravity(boolean gravity);

    public abstract Object getMetaEntitySilenced(boolean silenced);

    public abstract Object getMetaEntityCustomNameVisible(boolean visible);

    public abstract Object getMetaEntityCustomName(String name);

    public abstract Object getMetaArmorStandProperties(boolean isSmall, boolean hasArms, boolean noBasePlate, boolean marker);

}