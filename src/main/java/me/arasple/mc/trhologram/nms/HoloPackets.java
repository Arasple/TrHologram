package me.arasple.mc.trhologram.nms;

import io.izzel.taboolib.module.inject.TInject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Arasple
 * @date 2020/1/29 20:12
 */
public abstract class HoloPackets {

    @TInject(asm = "me.arasple.mc.trhologram.nms.imp.HoloPacketsImp")
    private static HoloPackets instance;

    public static HoloPackets getInst() {
        return instance;
    }

    /**
     * 生成一个盔甲架实体数据包
     *
     * @param location 坐标
     * @return EntityArmorStand
     */
    public abstract Object createArmorStand(Location location);

    /**
     * 设置一个盔甲架实体的显示名称
     *
     * @param armorstand EntityArmorStand
     * @param name       Display Name
     */
    public abstract void setArmorstandName(Object armorstand, String name);

    /**
     * 设置盔甲架名称是否可视
     *
     * @param armorstand EntityArmorStand
     * @param visible    Display Name
     */
    public abstract void setArmorstandVisible(Object armorstand, boolean visible);

    /**
     * 为玩家显示盔甲架实体
     *
     * @param armorstand EntityArmorStand
     * @param players    Players
     */
    public abstract void displayArmorstand(Object armorstand, Player... players);

    /**
     * 发送数据包更新盔甲架实体的属性
     *
     * @param armorstand EntityArmorStand
     * @param players    Players
     */
    public abstract void updateArmorstand(Object armorstand, Player... players);

    /**
     * 发送摧毁盔甲架实体的数据包
     *
     * @param armorstand EntityArmorStand
     * @param players    Players
     */
    public abstract void destroyArmorstand(Object armorstand, Player... players);

    /**
     * 发送传送盔甲架实体的数据包
     *
     * @param armorstand  盔甲架实体
     * @param destination 目的地
     * @param players     玩家
     */
    public abstract void teleportArmorstand(Object armorstand, Location destination, Player... players);

    /**
     * 取得实体 ID
     *
     * @param armorstand EntityArmorStand
     * @return Entity ID
     */
    public abstract int getEntityId(Object armorstand);

}
