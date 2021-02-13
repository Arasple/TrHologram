package me.arasple.mc.trhologram.module.condition

import io.izzel.taboolib.kotlin.kether.KetherShell
import io.izzel.taboolib.util.Coerce
import me.arasple.mc.trhologram.api.base.BaseCondition
import me.arasple.mc.trhologram.module.service.Performance
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/2/10 11:02
 */
inline class Condition(private val expression: String) : BaseCondition {

    override fun eval(player: Player): CompletableFuture<Boolean> {
        return if (expression.isEmpty()) CompletableFuture.completedFuture(true)
        else eval(player, expression)
    }

    override fun toString(): String {
        return expression
    }

    companion object {

        fun eval(player: Player, script: String): CompletableFuture<Boolean> {
            Performance.MIRROR.check("Hologram:Handler:ScriptEval") {
                return KetherShell.eval(script, namespace = listOf("trhologram", "trmenu")) {
                    sender = player
                }.thenApply {
                    Coerce.toBoolean(it)
                }
            }
            throw Exception()
        }

    }

}