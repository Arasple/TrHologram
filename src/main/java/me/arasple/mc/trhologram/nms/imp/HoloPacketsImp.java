package me.arasple.mc.trhologram.nms.imp;

import me.arasple.mc.trhologram.nms.HoloPackets;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/29 20:27
 */
public class HoloPacketsImp extends HoloPackets {

    @Override
    public Object createArmorStand(Location location) {
        EntityArmorStand armorStand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
        armorStand.setBasePlate(false);
        armorStand.setArms(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        return armorStand;
    }

    @Override
    public void setArmorstandName(Object armorstand, String name) {
        ((Entity) armorstand).setCustomName(new ChatComponentText(name));
    }

    @Override
    public void setArmorstandVisible(Object armorstand, boolean visible) {
        ((EntityArmorStand) armorstand).setCustomNameVisible(visible);
    }

    @Override
    public void displayArmorstand(Object armorstand, Player... players) {
        Object packet = new PacketPlayOutSpawnEntityLiving((EntityArmorStand) armorstand);
        for (Player player : players) {
            sendPacket(player, packet);
        }
        updateArmorstand(armorstand, players);
    }

    @Override
    public void updateArmorstand(Object armorstand, Player... players) {
        EntityArmorStand armorStand = (EntityArmorStand) armorstand;
        Object packet = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);
        for (Player player : players) {
            sendPacket(player, packet);
        }
    }

    @Override
    public void destroyArmorstand(Object armorstand, Player... players) {
        Object packet = new PacketPlayOutEntityDestroy(((EntityArmorStand) armorstand).getId());
        for (Player player : players) {
            sendPacket(player, packet);
        }
    }

    @Override
    public void teleportArmorstand(Object armorstand, Location destination, Player... players) {
        ((EntityArmorStand) armorstand).setLocation(destination.getX(), destination.getY(), destination.getZ(), destination.getYaw(), destination.getPitch());
        Object packet = new PacketPlayOutEntityTeleport((Entity) armorstand);
        for (Player player : players) {
            sendPacket(player, packet);
        }
    }

    @Override
    public int getEntityId(Object armorstand) {
        return ((Entity) armorstand).getId();
    }

    private void sendPacket(Player player, Object packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) packet);
    }

}
