package me.arasple.mc.trhologram.nms;

import io.izzel.taboolib.Version;
import io.izzel.taboolib.module.lite.SimpleReflection;
import io.izzel.taboolib.module.lite.SimpleVersionControl;
import io.izzel.taboolib.module.locale.TLocale;
import me.arasple.mc.trhologram.TrHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2020/2/15 21:02
 * -
 * 部分代码参考自 https://github.com/VolmitSoftware/Mortar
 */
public abstract class HoloPackets {

    private static HoloPackets inst;

    public static void init() {
        try {
            String version = Version.isAfter(Version.v1_13) ? "15" : Version.isAfter(Version.v1_9) ? "12" : "8";
            inst = (HoloPackets) SimpleVersionControl.createNMS("me.arasple.mc.trhologram.nms.impl.HoloPacketsImp_v1_" + version).translate(TrHologram.getPlugin()).newInstance();
            TLocale.sendToConsole("PLUGIN.LOADING", Bukkit.getVersion());
        } catch (Throwable e) {
            TrHologram.LOGGER.error("An error occurred while adapting your server version " + Version.getBukkitVersion() + ", please make sure your version is supported. Plugin will not work");
            e.printStackTrace();
        }
    }

    public static HoloPackets getInst() {
        return inst;
    }

    public abstract void spawnArmorStand(Player player, int entityId, UUID uuid, Location location);

    public abstract void spawnItem(Player player, int entityId, UUID uuid, Location location, ItemStack itemStack);

    public abstract void destroyEntity(Player player, int entityId);

    public abstract void initArmorStandAsHologram(Player player, int entityId);

    public abstract void updateArmorStandDisplayName(Player player, int entityId, String name);

    public abstract void updateArmorStandLocation(Player player, int entityId, Location location);

    public abstract void updatePassengers(Player player, int entityId, int... passengers);

    public abstract void updateArmorStandEquipmentItem(Player player, int entityId, EquipmentSlot slot, ItemStack itemStack);

    public abstract void sendEntityMetadata(Player player, int entityId, Object... objects);

    public abstract Object getMetaEntityItemStack(ItemStack itemStack);

    public abstract Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra);

    public abstract Object getMetaEntityGravity(boolean noGravity);

    public abstract Object getMetaEntitySilenced(boolean silenced);

    public abstract Object getMetaEntityCustomNameVisible(boolean visible);

    public abstract Object getMetaEntityCustomName(String name);

    public abstract Object getMetaArmorStandProperties(boolean isSmall, boolean hasArms, boolean noBasePlate, boolean marker);


    public Object setPacket(Class<?> nms, Object packet, Map<String, Object> sets) {
        sets.forEach((key, value) -> SimpleReflection.setFieldValue(nms, packet, key, value));
        return packet;
    }


}
