package me.arasple.mc.trhologram.nms.impl;

import io.izzel.taboolib.module.lite.SimpleReflection;
import io.izzel.taboolib.module.packet.TPacketHandler;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.nms.HoloPackets;
import me.arasple.mc.trhologram.utils.MapBuilder;
import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.DataWatcher.Item;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @author Arasple
 * @date 2020/2/1 22:40
 */
public class HoloPacketsImp_v1_12 extends HoloPackets {

    static {
        SimpleReflection.checkAndSave(
                PacketPlayOutSpawnEntity.class,
                PacketPlayOutEntityTeleport.class,
                PacketPlayOutEntityMetadata.class,
                PacketPlayOutMount.class
        );
    }

    @Override
    public void spawnArmorStand(Player player, int entityId, UUID uuid, Location location) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutSpawnEntity.class, new PacketPlayOutSpawnEntity(), new MapBuilder()
                .put("a", entityId)
                .put("b", uuid)
                .put("c", location.getX())
                .put("d", location.getY())
                .put("e", location.getZ())
                .put("f", 0)
                .put("g", 0)
                .put("h", 0)
                .put("i", 0)
                .put("j", 0)
                .put("k", 78)
                .put("l", 0)
                .build())
        );
        initArmorStandAsHologram(player, entityId);
    }

    @Override
    public void spawnItem(Player player, int entityId, UUID uuid, Location location, ItemStack itemStack) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutSpawnEntity.class, new PacketPlayOutSpawnEntity(), new MapBuilder()
                .put("a", entityId)
                .put("b", uuid)
                .put("c", location.getX())
                .put("d", location.getY())
                .put("e", location.getZ())
                .put("f", 0)
                .put("g", 0)
                .put("h", 0)
                .put("i", 0)
                .put("j", 0)
                .put("k", 2)
                .put("l", 0)
                .build())
        );
        sendEntityMetadata(player, entityId, getMetaEntityGravity(true), getMetaEntityItemStack(itemStack));
    }

    @Override
    public void destroyEntity(Player player, int entityId) {
        TPacketHandler.sendPacket(player, new PacketPlayOutEntityDestroy(entityId));
    }

    @Override
    public void initArmorStandAsHologram(Player player, int entityId) {
        sendEntityMetadata(player, entityId,
                getMetaEntityGravity(false),
                getMetaEntityCustomNameVisible(true),
                getMetaEntitySilenced(true),
                getMetaEntityProperties(false, false, true, false, true, false, false),
                getMetaArmorStandProperties(TrHologram.SETTINGS.getBoolean("ARMORSTAND-SMALL", true), false, true, false)
        );
    }

    @Override
    public void updateArmorStandDisplayName(Player player, int entityId, String name) {
        sendEntityMetadata(player, entityId, getMetaEntityCustomName(name));
    }

    @Override
    public void updateArmorStandLocation(Player player, int entityId, Location location) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutEntityTeleport.class, new PacketPlayOutEntityTeleport(), new MapBuilder()
                .put("a", entityId)
                .put("b", location.getX())
                .put("c", location.getY())
                .put("d", location.getZ())
                .put("e", (byte) 0)
                .put("f", (byte) 0)
                .put("g", false)
                .build())
        );
    }

    @Override
    public void updatePassengers(Player player, int entityId, int... passengers) {
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutMount.class, new PacketPlayOutMount(), new MapBuilder()
                .put("a", entityId)
                .put("b", passengers)
                .build())
        );
    }

    @Override
    public void updateArmorStandEquipmentItem(Player player, int entityId, EquipmentSlot slot, ItemStack itemStack) {
        TPacketHandler.sendPacket(player, new PacketPlayOutEntityEquipment(entityId, EnumItemSlot.valueOf(slot.name()), CraftItemStack.asNMSCopy(itemStack)));
    }

    @Override
    public void sendEntityMetadata(Player player, int entityId, Object... objects) {
        List<Item<?>> items = new ArrayList<>();
        for (Object object : objects) {
            items.add((Item<?>) object);
        }
        TPacketHandler.sendPacket(player, setPacket(PacketPlayOutEntityMetadata.class, new PacketPlayOutEntityMetadata(), new MapBuilder()
                .put("a", entityId)
                .put("b", items)
                .build())
        );
    }

    @Override
    public Object getMetaEntityItemStack(ItemStack itemStack) {
        return new DataWatcher.Item<>(new DataWatcherObject<>(7, DataWatcherRegistry.f), CraftItemStack.asNMSCopy(itemStack));
    }

    @Override
    public Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra) {
        byte bits = 0;
        bits += onFire ? 1 : 0;
        bits += crouched ? 2 : 0;
        bits += sprinting ? 8 : 0;
        bits += swimming ? 10 : 0;
        bits += glowing ? 20 : 0;
        bits += invisible ? 40 : 0;
        bits += flyingElytra ? 80 : 0;

        return new Item<>(new DataWatcherObject<>(0, DataWatcherRegistry.a), bits);
    }

    @Override
    public Object getMetaEntityGravity(boolean noGravity) {
        return new Item<>(new DataWatcherObject<>(5, DataWatcherRegistry.h), noGravity);
    }

    @Override
    public Object getMetaEntitySilenced(boolean silenced) {
        return new Item<>(new DataWatcherObject<>(4, DataWatcherRegistry.h), silenced);
    }

    @Override
    public Object getMetaEntityCustomNameVisible(boolean visible) {
        return new Item<>(new DataWatcherObject<>(3, DataWatcherRegistry.h), visible);
    }

    @Override
    public Object getMetaEntityCustomName(String name) {
        return new Item<>(new DataWatcherObject<>(2, DataWatcherRegistry.d), name);
    }

    @Override
    public Object getMetaArmorStandProperties(boolean isSmall, boolean hasArms, boolean noBasePlate, boolean marker) {
        byte bits = 0;
        bits += isSmall ? 1 : 0;
        bits += hasArms ? 4 : 0;
        bits += noBasePlate ? 8 : 0;
        bits += marker ? 10 : 0;
        return new Item<>(new DataWatcherObject<>(11, DataWatcherRegistry.a), bits);
    }

}
