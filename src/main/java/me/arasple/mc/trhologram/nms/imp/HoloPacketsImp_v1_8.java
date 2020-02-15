package me.arasple.mc.trhologram.nms.imp;

import io.izzel.taboolib.module.lite.SimpleReflection;
import io.izzel.taboolib.module.packet.TPacketHandler;
import me.arasple.mc.trhologram.nms.HoloPackets;
import me.arasple.mc.trhologram.utils.MapBuilder;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
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
public class HoloPacketsImp_v1_8 extends HoloPackets {

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
                .put("b", location.getX())
                .put("c", location.getY())
                .put("d", location.getZ())
                .put("j", 78)
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
        TPacketHandler.sendPacket(player, new PacketPlayOutEntityEquipment(entityId, 5, CraftItemStack.asNMSCopy(itemStack)));
    }

    @Override
    public void sendEntityMetadata(Player player, int entityId, Object... objects) {
        List<DataWatcher.WatchableObject> items = new ArrayList<>();
        for (Object object : objects) {
            items.add((DataWatcher.WatchableObject) object);
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

        return new DataWatcher.WatchableObject(0, 0, bits);
    }

    @Override
    public Object getMetaEntityGravity(boolean gravity) {
        return null;
    }

    @Override
    public Object getMetaEntitySilenced(boolean silenced) {
        return new DataWatcher.WatchableObject(0, 4, (byte) (silenced ? 1 : 0));
    }

    @Override
    public Object getMetaEntityCustomNameVisible(boolean visible) {
        return new DataWatcher.WatchableObject(0, 3, (byte) (visible ? 1 : 0));
    }

    @Override
    public Object getMetaEntityCustomName(String name) {
        return new DataWatcher.WatchableObject(4, 2, name);
    }

    @Override
    public Object getMetaArmorStandProperties(boolean isSmall, boolean hasArms, boolean noBasePlate, boolean marker) {
        byte bits = 0;
        bits += isSmall ? 1 : 0;
        bits += hasArms ? 2 : 0;
        bits += noBasePlate ? 8 : 0;
        bits += marker ? 10 : 0;

        return new DataWatcher.WatchableObject(0, 10, bits);
    }

    private Object setPacket(Class<?> nms, Object packet, Map<String, Object> sets) {
        sets.forEach((key, value) -> SimpleReflection.setFieldValue(nms, packet, key, value));
        return packet;
    }

}
