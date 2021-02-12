package me.arasple.mc.trhologram.api.hologram

import io.izzel.taboolib.kotlin.Tasks
import me.arasple.mc.trhologram.api.Position
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.api.nms.packet.PacketEntityDestroy
import me.arasple.mc.trhologram.util.Tasks.shut
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import kotlin.properties.Delegates

/**
 * @author Arasple
 * @date 2021/2/10 10:36
 */
abstract class HologramComponent(
    val position: Position,
    tick: Long = -1,
    val onTick: (HologramComponent) -> Unit = {}
) {

    abstract fun spawn(player: Player)

    abstract fun onTick()

    val entityId by lazy { TrHologramAPI.getIndex() }

    internal val viewers = mutableSetOf<String>()

    private var task: BukkitTask? = null

    var period by Delegates.observable(tick) { _, _, _ ->
        deployment()
    }

    init {
        deployment()
    }

    private fun deployment() {
        task.shut()
        if (period > 0) task = Tasks.timer(period, period, true) {
            viewers.removeIf {
                val player = Bukkit.getPlayerExact(it)
                player == null || !player.isOnline
            }
            onTick()
            onTick(this)
        }
    }

    fun destroy(player: Player) {
        PacketEntityDestroy(entityId).send(player)
        viewers.remove(player.name)
    }

    fun destroy() {
        task.shut()
        forViewers { destroy(it) }
    }

    fun forViewers(viewer: (Player) -> Unit) {
        viewers.mapNotNull { Bukkit.getPlayerExact(it) }.forEach(viewer)
    }

}