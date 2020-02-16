package me.arasple.mc.trhologram.hologram;

import com.google.common.collect.Lists;
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils;
import io.izzel.taboolib.util.Strings;
import io.izzel.taboolib.util.lite.Sounds;
import me.arasple.mc.trhologram.TrHologram;
import me.arasple.mc.trhologram.action.ActionGroups;
import me.arasple.mc.trhologram.action.TrAction;
import me.arasple.mc.trhologram.item.Mat;
import me.arasple.mc.trhologram.utils.JavaScript;
import me.arasple.mc.trhologram.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Arasple
 * @date 2020/1/29 20:06
 */
public class Hologram {

    private static List<Hologram> trHolograms;

    private String name;
    private List<UUID> viewers;
    private List<HologramContent> contents;
    private List<String> rawContents;
    private List<ActionGroups> actions;
    private Location location;
    private String viewCondition;
    private String viewDistance;
    private double finalViewDistance;
    private String loadedFrom;

    public Hologram(String name, Location location, String... contents) {
        this(name, location, Arrays.asList(contents), null, null, null);
    }

    public Hologram(String name, Location location, List<String> contents) {
        this(name, location, contents, null, null, null);
    }

    public Hologram(String name, Location location, List<String> contents, List<ActionGroups> actions) {
        this(name, location, contents, actions, null, null);
    }

    public Hologram(String name, Location location, List<String> contents, List<ActionGroups> actions, String viewCondition, String viewDistance) {
        this.name = name;
        this.location = location;
        this.contents = HologramContent.createList(contents);
        this.rawContents = contents;
        this.actions = actions;
        this.viewers = Lists.newArrayList();
        this.viewCondition = viewCondition;
        this.viewDistance = viewDistance;
        this.finalViewDistance = NumberUtils.toDouble(viewDistance, -1);

        trHolograms.add(this);
    }

    /**
     * 试图为这些玩家显示全息图
     *
     * @param players 玩家
     */
    public void display(Player... players) {
        if (!location.getChunk().isLoaded()) {
            return;
        }
        for (Player player : players) {
            if (viewers.contains(player.getUniqueId())) {
                contents.forEach(l -> l.update(player));
            } else {
                refreshContents(player);
                viewers.add(player.getUniqueId());
            }
        }
    }

    private void refreshContents(Player player) {
        Location location = this.location.clone();
        double y = TrHologram.SETTINGS.getDouble("OPTIONS.ARMORSTAND-DISTANCE", 0.25);
        for (int i = 0; i < getContents().size(); i++) {
            HologramContent content = getContents().get(i);
            if (Strings.nonEmpty(content.getText())) {
                if (content.isSpawned(player)) {
                    content.updateLocation(location, player);
                    content.update(player);
                } else {
                    content.display(location, player);
                }
            }
            location = location.clone().subtract(0, y, 0);
        }
    }

    /**
     * 为已展示的玩家更新全息图信息, 或者隐藏不再满足条件的全息图观察者
     */
    public void update() {
        if (!location.getChunk().isLoaded()) {
            return;
        }
        getViewersAsPlayer().forEach(player -> {
            if (!isVisible(player)) {
                destroy(player);
            } else {
                getContents().forEach(i -> i.update(player));
            }
        });
        viewers.removeIf(uuid -> !Bukkit.getOfflinePlayer(uuid).isOnline() || !isVisible(Bukkit.getPlayer(uuid)));
    }

    /**
     * 摧毁玩家该显全息图
     *
     * @param players
     */
    public void destroy(Player... players) {
        contents.forEach(l -> l.destroy(players));
        for (Player player : players) {
            viewers.remove(player.getUniqueId());
        }
    }

    public void destroyAll() {
        getViewersAsPlayer().forEach(this::destroy);
    }

    /**
     * 玩家是否满足观察该全息图的条件
     *
     * @param player 玩家
     * @return 是否满足
     */
    public boolean isVisible(Player player) {
        if (player.getLocation().getWorld() != location.getWorld()) {
            return false;
        }
        double distance = finalViewDistance > 0 ? finalViewDistance : NumberUtils.toDouble(String.valueOf(JavaScript.run(player, viewDistance)), -1);
        return (distance <= 0 || player.getLocation().distance(location) <= distance) && (boolean) JavaScript.run(player, viewCondition);
    }

    /*
    GETTERS & SETTERS
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UUID> getViewers() {
        return viewers;
    }

    public void setViewers(List<UUID> viewers) {
        this.viewers = viewers;
    }

    public List<Player> getViewersAsPlayer() {
        List<Player> players = Lists.newArrayList();
        viewers.forEach(viewer -> {
            Player player = Bukkit.getPlayer(viewer);
            if (player != null && player.isOnline()) {
                players.add(player);
            }
        });
        return players;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (!Locations.equals(this.location, location)) {
            this.location = location;
            getViewersAsPlayer().forEach(this::refreshContents);
        } else {
            this.location = location;
        }
    }

    public List<HologramContent> getContents() {
        return contents;
    }

    public static List<Hologram> getTrHolograms() {
        return trHolograms;
    }

    public void setContents(List<String> contents) {
        this.rawContents = contents;
        while (contents.size() < getContents().size()) {
            HologramContent content = getContents().get(getContents().size() - 1);
            getViewersAsPlayer().forEach(content::destroy);
            getContents().remove(getContents().size() - 1);
        }
        int size = getContents().size();
        for (int i = 0; i < contents.size(); i++) {
            if (size > i) {
                HologramContent content = getContents().get(i);
                if (content.getMat() != null || Mat.readMat(contents.get(i)) != null) {
                    getViewersAsPlayer().forEach(content::destroy);
                    getContents().set(i, new HologramContent(contents.get(i)));
                } else {
                    content.setText(contents.get(i));
                }
            } else {
                HologramContent content = new HologramContent(contents.get(i));
                getContents().add(content);
            }
        }
        getViewersAsPlayer().forEach(this::refreshContents);
    }

    public List<String> getRawContents() {
        return rawContents;
    }

    public List<ActionGroups> getActions() {
        return actions;
    }

    public void setActions(List<ActionGroups> actions) {
        this.actions = actions;
    }

    public String getViewCondition() {
        return viewCondition;
    }

    public void setViewCondition(String viewCondition) {
        this.viewCondition = viewCondition;
    }

    public String getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(String viewDistance) {
        this.viewDistance = viewDistance;
        this.finalViewDistance = NumberUtils.toDouble(viewDistance, -1);
    }

    public Object getExactViewDistance() {
        return NumberUtils.isParsable(getViewDistance()) ? NumberUtils.toDouble(getViewDistance(), -1) : getViewDistance();
    }

    public double getFinalViewDistance() {
        return finalViewDistance;
    }

    public void setFinalViewDistance(double finalViewDistance) {
        this.finalViewDistance = finalViewDistance;
    }

    public String getLoadedFrom() {
        return loadedFrom;
    }

    public void setLoadedFrom(String loadedFrom) {
        this.loadedFrom = loadedFrom;
    }

    public boolean isViewing(Player player) {
        return getViewers().contains(player.getUniqueId());
    }

    public List<String> getContentsAsList() {
        List<String> contents = Lists.newArrayList();
        getContents().forEach(h -> contents.add(h.getText()));
        return contents;
    }

    public void applySection(YamlConfiguration data) {
        setLocation(Locations.from(data.getString("location")));
        setContents(data.getStringList("contents"));
        setActions(TrAction.readActionGroups(data.get("actions")));
        setViewCondition(data.getString("viewCondition"));
        setViewDistance(data.getString("viewDistance"));
        Location location = getLocation();
        if (TrHologram.SETTINGS.getBoolean("OPTIONS.AUTO-RELOAD-SOUND")) {
            Sounds.ITEM_BOTTLE_FILL.playSound(location, 1, 0);
        }
    }

    public void applySection() {
        applySection(YamlConfiguration.loadConfiguration(new File(getLoadedFrom())));
    }

    public void runTask() {
        getContents().forEach(HologramContent::runTask);
    }

    public void setContents(String... contents) {
        setContents(Arrays.asList(contents));
    }

    public void cancelTask() {
        getContents().forEach(HologramContent::cancelTask);
    }

    public void delete() {
        cancelTask();
        destroyAll();
        trHolograms.remove(this);
    }

    @Override
    public String toString() {
        return "Hologram{" +
                "name='" + name + '\'' +
                ", viewers=" + viewers +
                ", contents=" + contents +
                ", rawContents=" + rawContents +
                ", actions=" + actions +
                ", location=" + location +
                ", viewCondition='" + viewCondition + '\'' +
                ", viewDistance='" + viewDistance + '\'' +
                ", finalViewDistance=" + finalViewDistance +
                ", loadedFrom='" + loadedFrom + '\'' +
                '}';
    }

}
