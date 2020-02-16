package me.arasple.mc.trhologram.hologram;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Numbers;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.item.Mat;
import me.arasple.mc.trhologram.nms.HoloPackets;
import me.arasple.mc.trhologram.utils.JavaScript;
import me.arasple.mc.trhologram.utils.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arasple
 * @date 2020/1/29 20:07
 */
public class HologramContent {

    // Random and uniqure starts entity ID

    private static final Pattern VIEW_CONDITION = Pattern.compile("<(?i)(require(ment)?|condition):( )?(.+>)");
    private static final Pattern UPDATE_PERIOD = Pattern.compile("<(?i)(update|refresh):( )?([0-9]+[.]?[0-9]*>)");
    private static int ORIGINAL_ID = 119789 + Numbers.getRandomInteger(0, 7763);
    private int id;
    private UUID uuid;

    private String text;
    private Mat mat;
    private String viewCondition;
    private List<UUID> viewing;
    private List<UUID> hide;
    private BukkitRunnable task;
    private int update;

    public HologramContent(String text) {
        this.id = ORIGINAL_ID++;
        this.uuid = UUID.randomUUID();
        this.viewing = Lists.newArrayList();
        this.hide = Lists.newArrayList();
        this.update = -1;
        setText(text);
    }

    public static List<HologramContent> createList(List<String> lines) {
        List<HologramContent> holoLines = Lists.newArrayList();
        lines.forEach(l -> holoLines.add(new HologramContent(l)));
        return holoLines;
    }

    public void updateLocation(Location location, Player... players) {
        for (Player player : players) {
            getHoloPackets().updateArmorStandLocation(player, id, location);
        }
    }

    public void update(Player... players) {
        for (Player player : players) {
            if (getMat() != null) {
                getHoloPackets().updateArmorStandEquipmentItem(player, id, EquipmentSlot.HEAD, getMat().createItem(player));
            } else {
                getHoloPackets().updateArmorStandDisplayName(player, id, Vars.replace(player, text));
            }
        }
    }

    public void display(Location location, Player... players) {
        for (Player player : players) {
            if (!this.viewing.contains(player.getUniqueId())) {
                getHoloPackets().spawnArmorStand(player, id, uuid, location);
                if (mat != null) {
                    getHoloPackets().sendEntityMetadata(player, id, getHoloPackets().getMetaEntityCustomNameVisible(false));
                }
                viewing.add(player.getUniqueId());
            }
        }
        update(players);
    }

    public void destroy(Player... players) {
        for (Player player : players) {
            getHoloPackets().destroyArmorStand(player, id);
            this.viewing.remove(player.getUniqueId());
        }
    }

    public boolean isSpawned(Player player) {
        return this.viewing.contains(player.getUniqueId());
    }

    public void cancelTask() {
        try {
            task.cancel();
        } catch (Throwable ignored) {
        }
    }

    public void runTask() {
        cancelTask();
        if (update < 0) {
            return;
        }
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (viewing.isEmpty()) {
                    return;
                }
                getViewersAsPlayer().forEach(player -> {
                    if (!isVisible(player) && !hide.contains(player.getUniqueId())) {
                        getHoloPackets().sendEntityMetadata(player, id, getHoloPackets().getMetaEntityCustomNameVisible(false));
                        hide.add(player.getUniqueId());
                    } else {
                        if (isVisible(player) && hide.contains(player.getUniqueId())) {
                            getHoloPackets().sendEntityMetadata(player, id, getHoloPackets().getMetaEntityCustomNameVisible(true));
                            hide.remove(player.getUniqueId());
                        }
                        update(player);
                    }
                });
            }
        };
        task.runTaskTimerAsynchronously(TrHologram.getPlugin(), update, update);
    }

    private boolean isVisible(Player player) {
        return (boolean) JavaScript.run(player, viewCondition);
    }

    private void readOptions() {
        Matcher updateOption = UPDATE_PERIOD.matcher(this.text);
        if (updateOption.find()) {
            String find = updateOption.group();
            int updatePeriod = NumberUtils.toInt(find.replace(">", "").split(":", 2)[1], -1);
            setUpdate(updatePeriod);
            this.text = this.text.replace(find, "");
        }
        Matcher require = VIEW_CONDITION.matcher(this.text);
        if (require.find()) {
            String find = require.group();
            setViewCondition(find.substring(0, find.length() - 1).split(":", 2)[1]);
            this.text = this.text.replace(find, "");
        }
    }

    private void readMat() {
        this.mat = Mat.readMat(this.text);
    }

    /*
    GETTERS & SETTERS
     */

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        readOptions();
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

    public int getUpdate() {
        return update;
    }

    public void setUpdate(int update) {
        boolean restartTask = this.update != update;
        this.update = update;
        if (restartTask) {
            runTask();
        }
    }

    public String getViewCondition() {
        return this.viewCondition;
    }

    public void setViewCondition(String viewCondition) {
        this.viewCondition = viewCondition;
    }

    public List<Player> getViewersAsPlayer() {
        List<Player> players = Lists.newArrayList();
        viewing.forEach(viewer -> {
            Player player = Bukkit.getPlayer(viewer);
            if (player != null && player.isOnline()) {
                players.add(player);
            }
        });
        return players;
    }

    public boolean isEmpty() {
        return Strings.isEmpty(getText()) && getMat() == null;
    }

    private HoloPackets getHoloPackets() {
        return HoloPackets.getInst();
    }

}
