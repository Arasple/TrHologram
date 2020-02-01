package me.arasple.mc.trhologram.nms.imp;

import io.izzel.taboolib.Version;
import me.arasple.mc.trhologram.nms.HoloPackets;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Arasple
 * @date 2020/1/29 20:27
 */
public class HoloPacketsImp extends HoloPackets {

    private Method setCustomNameForOldVersion;


    @Override
    public Object createArmorStand(Location location) {
        Object armorStand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
        ((EntityArmorStand) armorStand).setBasePlate(false);
        ((EntityArmorStand) armorStand).setArms(false);
        ((EntityArmorStand) armorStand).setCustomNameVisible(true);
        ((EntityArmorStand) armorStand).setInvisible(true);
        return armorStand;
    }

    @Override
    public void setArmorstandName(Object armorstand, String name) {
        if (Version.isAfter(Version.v1_13)) {
            ((Entity) armorstand).setCustomName(new ChatComponentText(name));
        } else {
            try {
                if (setCustomNameForOldVersion == null) {
                    setCustomNameForOldVersion = EntityArmorStand.class.getMethod("setCustomName", String.class);
                }
                setCustomNameForOldVersion.invoke(armorstand, name);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setArmorstandVisible(Object armorstand, boolean visible) {
        ((EntityArmorStand) armorstand).setCustomNameVisible(visible);
    }

    @Override
    public void displayArmorstand(Object armorstand, Player player) {
        Object packet = new PacketPlayOutSpawnEntityLiving((EntityArmorStand) armorstand);
        sendPacket(player, packet);
        updateArmorstand(armorstand, player);
    }

    @Override
    public void updateArmorstand(Object armorstand, Player player) {
        Object packet = new PacketPlayOutEntityMetadata(((EntityArmorStand) armorstand).getId(), ((EntityArmorStand) armorstand).getDataWatcher(), true);
        sendPacket(player, packet);
    }

    @Override
    public void destroyArmorstand(Object armorstand, Player player) {
        Object packet = new PacketPlayOutEntityDestroy(((EntityArmorStand) armorstand).getId());
        sendPacket(player, packet);

    }

    @Override
    public void teleportArmorstand(Object armorstand, Location destination, Player player) {
        ((EntityArmorStand) armorstand).setLocation(destination.getX(), destination.getY(), destination.getZ(), destination.getYaw(), destination.getPitch());
        Object packet = new PacketPlayOutEntityTeleport((Entity) armorstand);
        sendPacket(player, packet);
    }

    @Override
    public int getEntityId(Object armorstand) {
        return ((Entity) armorstand).getId();
    }

    private void sendPacket(Player player, Object packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) packet);
    }

}
