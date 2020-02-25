package me.arasple.mc.trhologram.hologram

import com.google.common.collect.Lists
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import io.izzel.taboolib.util.Strings
import io.izzel.taboolib.util.lite.Numbers
import me.arasple.mc.trhologram.TrHologram
import me.arasple.mc.trhologram.item.Mat
import me.arasple.mc.trhologram.nms.HoloPackets
import me.arasple.mc.trhologram.utils.JavaScript
import me.arasple.mc.trhologram.utils.Vars
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.regex.Pattern

/**
 * @author Arasple
 * @date 2020/2/20 15:14
 */
open class HologramLine(private var text: String, val hologram: Hologram) {

    // id: Int, itemId: Int, uid: UUID, itemUid: UUID,
    // viewing: MutableList<UUID>, hided: MutableList<UUID>, update: Int, condition: String, distance: String, floating: Boolean, task: BukkitRunnable

    var id: Int = ORIGINAL_ID++
    private val uid: UUID = UUID.randomUUID()
    private var itemId: Int = ORIGINAL_ID++
    private val itemUid: UUID = UUID.randomUUID()
    val viewing: MutableList<UUID> = mutableListOf()
    val hided: MutableList<UUID> = mutableListOf()
    private var update: Int = -1
    private var condition: String = ""
    var mat: Mat? = null
    private var floating: Boolean = false
    private var task: BukkitRunnable? = null

    companion object {

        val VIEW_CONDITION: Pattern = Pattern.compile("<(?i)(require(ment)?|condition):( )?(.+>)")
        val UPDATE_PERIOD: Pattern = Pattern.compile("<(?i)(update|refresh):( )?([0-9]+[.]?[0-9]*>)")
        val FLOATING: Pattern = Pattern.compile("<(?i)(float(ing)?|)(:)?( )?>")

        var ORIGINAL_ID = 119789 + Numbers.getRandomInteger(0, 7763)

        fun createLines(hologram: Hologram, lines: List<String>): MutableList<HologramLine> {
            val hologramLines: MutableList<HologramLine> = Lists.newArrayList()
            lines.forEach { l: String -> hologramLines.add(HologramLine(l, hologram)) }
            return hologramLines
        }

    }

    init {
        update = -1
        condition = ""
        floating = false
        initSettings()
    }

    /**
     * 显示全息图
     */
    fun display(location: Location, vararg players: Player) {
        for (player in players) {
            if (!viewing.contains(player.uniqueId)) {
                getPackets().spawnArmorStand(player, id, uid, location)
                if (mat != null) {
                    getPackets().sendEntityMetadata(player, id, getPackets().getMetaEntityCustomNameVisible(false))
                    if (floating) {
                        getPackets().spawnItem(player, itemId, itemUid, location, mat!!.createItem(player))
                        Bukkit.getScheduler().runTaskLaterAsynchronously(TrHologram.getPlugin(), Runnable { getPackets().updatePassengers(player, id, itemId) }, 1)
                    }
                }
                viewing.add(player.uniqueId)
            }
        }
        update(*players)
    }

    /**
     * 更新全息图文本、物品
     */
    fun update(vararg players: Player) {
        for (player in players) {
            if (mat != null) {
                if (floating) {
                    getPackets().sendEntityMetadata(player, itemId, getPackets().getMetaEntityItemStack(mat!!.createItem(player)))
                } else {
                    getPackets().updateArmorStandEquipmentItem(player, id, EquipmentSlot.HEAD, mat!!.createItem(player))
                }
            } else {
                getPackets().updateArmorStandDisplayName(player, id, Vars.replace(player, text))
            }
        }
    }

    /**
     * 摧毁全息图
     */
    fun destroy(player: Player) {
        if (itemId > 0) {
            getPackets().destroyEntity(player, itemId)
        }
        getPackets().destroyEntity(player, id)
        viewing.remove(player.uniqueId)
    }

    /**
     * 是否已为玩家刷出盔甲架
     */
    fun hasArmortsandSpawned(player: Player): Boolean {
        return viewing.contains(player.uniqueId)
    }

    /**
     *是否可视
     */
    fun isVisible(player: Player): Boolean {
        return JavaScript.run(player, condition) as Boolean
    }

    fun cancelTask() {
        try {
            task?.cancel()
        } catch (ignored: Throwable) {
        }
    }

    fun runTask() {
        cancelTask()
        if (update < 0) {
            return
        }
        task = object : BukkitRunnable() {
            override fun run() {
                if (viewing.isEmpty()) {
                    return
                }
                hologram.viewers.forEach { player: Player ->
                    if (!isVisible(player) && !hided.contains(player.uniqueId)) {
                        getPackets().sendEntityMetadata(player, id, getPackets().getMetaEntityCustomNameVisible(false))
                        hided.add(player.uniqueId)
                    } else {
                        if (isVisible(player) && hided.contains(player.uniqueId)) {
                            getPackets().sendEntityMetadata(player, id, getPackets().getMetaEntityCustomNameVisible(true))
                            hided.remove(player.uniqueId)
                        }
                        update(player)
                    }
                }
            }
        }
        task?.runTaskTimerAsynchronously(TrHologram.getPlugin(), update.toLong(), update.toLong())
    }

    /**
     * 更新全息图位置
     */
    fun updateLocation(location: Location, vararg players: Player) {
        for (player in players) {
            if (isViewing(player)) {
                getPackets().updateArmorStandLocation(player, id, location)
            }
        }
    }


    /**
     * 玩家是否可视该全息图
     */
    private fun isViewing(player: Player): Boolean {
        return viewing.contains(player.uniqueId)
    }

    fun updateDisplayText(text: String) {
        this.text = text
        initSettings()
    }

    fun updateTaskPeriod(period: Int) {
        val restartTask = update != period
        update = period
        if (restartTask) {
            runTask()
        }
    }

    private fun initSettings() {
        val floatingOption = FLOATING.matcher(text)
        if (floatingOption.find()) {
            floating = true
            text = text.replace(floatingOption.group(), "")
        }
        val require = VIEW_CONDITION.matcher(text)
        if (require.find()) {
            val find = require.group()
            condition = find.substring(0, find.length - 1).split(Regex(":"), 2).toTypedArray()[1]
            text = text.replace(find, "")
        }
        val updateOption = UPDATE_PERIOD.matcher(text)
        if (updateOption.find()) {
            val find = updateOption.group()
            val value = NumberUtils.toInt(find.replace(">", "").split(Regex(":"), 2).toTypedArray()[1], -1)
            if (update != value) {
                update = value
                runTask()
            }
            text = text.replace(find, "")
        }
        mat = Mat.mat(text)
    }

    fun isEmpty(): Boolean {
        return Strings.isEmpty(text) && mat == null
    }

    /*
    PRIVATE METHODS & UTILS
     */
    private fun getPackets(): HoloPackets {
        return HoloPackets.INSTANCE!!
    }


}