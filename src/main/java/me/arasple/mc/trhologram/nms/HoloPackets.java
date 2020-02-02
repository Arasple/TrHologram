package me.arasple.mc.trhologram.nms;

import io.izzel.taboolib.Version;
import io.izzel.taboolib.module.inject.TSchedule;
import me.arasple.mc.trhologram.nms.imp.HoloPacketsImp_v1_15;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * @author Arasple
 * @date 2020/1/29 20:12
 */
public abstract class HoloPackets {

    private static HoloPackets instance;

    @TSchedule
    static void init() {
        if (Version.getCurrentVersion() == Version.v1_15) {
            instance = new HoloPacketsImp_v1_15();
        } else if (Version.getCurrentVersion() == Version.v1_14) {

        } else {

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

    public abstract Vector getLookDirection(Object packet);


}