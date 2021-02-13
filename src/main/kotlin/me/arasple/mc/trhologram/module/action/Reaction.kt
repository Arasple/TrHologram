package me.arasple.mc.trhologram.module.action

import io.izzel.taboolib.kotlin.kether.KetherShell
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/2/11 16:31
 */
inline class Reaction(private val scripts: List<String>) {

    fun eval(player: Player): CompletableFuture<Any?> {
        return KetherShell.eval(scripts, true, listOf("trhologram")) {
            sender = player
        }
    }

}