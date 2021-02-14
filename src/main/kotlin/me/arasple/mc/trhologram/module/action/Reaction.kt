package me.arasple.mc.trhologram.module.action

import me.arasple.mc.trhologram.api.TrHologramAPI
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/2/11 16:31
 */
inline class Reaction(val kether: String) {

    constructor(kethers: List<String>) : this(kethers.joinToString("\n"))

    fun eval(player: Player): CompletableFuture<Any?> {
        return TrHologramAPI.eval(player, kether)
    }

}