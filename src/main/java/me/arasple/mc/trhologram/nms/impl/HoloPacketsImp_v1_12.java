package me.arasple.mc.trhologram.nms.impl;

import io.izzel.taboolib.module.lite.SimpleReflection;
import io.izzel.taboolib.module.packet.TPacketHandler;
import me.arasple.mc.trhologram.nms.HoloPackets;
import me.arasple.mc.trhologram.utils.MapBuilder;
import net.minecraft.server.v1_12_R1.DataWatcher.Item;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                PacketPlayOutEntityMetadata.class
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
    public void destroyArmorStand(Player player, int entityId) {
        TPacketHandler.sendPacket(player, new PacketPlayOutEntityDestroy(entityId));
    }

    @Override
    public void initArmorStandAsHologram(Player player, int entityId) {
        sendEntityMetadata(player, entityId,
                getMetaEntityProperties(false, true, true, true, true, true, true),
                getMetaEntityGravity(false),
                getMetaEntityCustomNameVisible(true),
                getMetaEntitySilenced(true),
                getMetaArmorStandProperties(true, false, true, true)
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
    public Object getMetaEntityProperties(boolean onFire, boolean crouched, boolean sprinting, boolean swimming, boolean invisible, boolean glowing, boolean flyingElytra) {
        byte bits = 0;
        bits += onFire ? 1 : 0;
        bits += crouched ? 2 : 0;
        bits += sprinting ? 8 : 0;
        bits += swimming ? 10 : 0;
        bits += invisible ? 20 : 0;
        bits += glowing ? 40 : 0;
        bits += flyingElytra ? 80 : 0;

        return new Item<>(new DataWatcherObject<>(0, DataWatcherRegistry.a), bits);
    }

    @Override
    public Object getMetaEntityGravity(boolean gravity) {
        return new Item<>(new DataWatcherObject<>(5, DataWatcherRegistry.h), gravity);
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
        return new Item<>(new DataWatcherObject<>(13, DataWatcherRegistry.a), bits);
    }

    private Object setPacket(Class<?> nms, Object packet, Map<String, Object> sets) {
        sets.forEach((key, value) -> SimpleReflection.setFieldValue(nms, packet, key, value));
        return packet;
    }

}
