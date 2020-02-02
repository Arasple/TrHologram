package me.arasple.mc.trhologram.hologram;

import com.google.common.collect.Lists;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Numbers;
import me.arasple.mc.trhologram.item.Mat;
import me.arasple.mc.trhologram.nms.HoloPackets;
import me.arasple.mc.trhologram.utils.Locations;
import me.arasple.mc.trhologram.utils.Vars;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2020/1/29 20:07
 */
public class HologramContent {

    private static int ORIGINAL_ID = 119789 + Numbers.getRandomInteger(1, 44959);

    private int id;
    private UUID uuid;
    private Location location;
    private String text;
    private Mat mat;
    private List<UUID> viewing;

    public HologramContent(String text) {
        setText(text);
        this.id = ORIGINAL_ID++;
        this.uuid = UUID.randomUUID();
        this.viewing = Lists.newArrayList();
    }

    public static List<HologramContent> createList(List<String> lines) {
        List<HologramContent> holoLines = Lists.newArrayList();
        lines.forEach(l -> holoLines.add(new HologramContent(l)));
        return holoLines;
    }

    public void updateLocation(Player... players) {
        for (Player player : players) {
            HoloPackets.getInst().updateArmorStandLocation(player, id, location);
        }
    }

    public void update(Player... players) {
        for (Player player : players) {
            if (getMat() != null) {
                HoloPackets.getInst().updateArmorStandEquipmentItem(player, id, EquipmentSlot.HEAD, getMat().createItem(player));
            } else {
                HoloPackets.getInst().updateArmorStandDisplayName(player, id, Vars.replace(player, text));
            }
        }
    }

    public void display(Player... players) {
        for (Player player : players) {
            if (!this.viewing.contains(player.getUniqueId())) {
                HoloPackets.getInst().spawnArmorStand(player, id, uuid, location);
                viewing.add(player.getUniqueId());
            }
        }
        update(players);
    }

    public void destroy(Player... players) {
        for (Player player : players) {
            HoloPackets.getInst().destroyArmorStand(player, id);
            this.viewing.remove(player.getUniqueId());
        }
    }

    private void readMat() {
        this.mat = Mat.readMat(this.text);
    }

    /*
    GETTERS & SETTERS
     */

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocation(Location location, List<Player> players) {
        this.location = getMat() != null ? location.add(0, 0, 0) : location;
        if (!Locations.equals(this.location, location) && players.size() > 0) {
            players.forEach(this::updateLocation);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        readMat();
    }

    public Mat getMat() {
        return mat;
    }

    public void setMat(Mat mat) {
        this.mat = mat;
    }

    public int getId() {
        return id;
    }

    public boolean isEmpty() {
        return Strings.isEmpty(getText()) && getMat() == null;
    }

}
