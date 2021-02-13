package me.arasple.mc.trhologram.module.condition

import io.izzel.taboolib.kotlin.kether.KetherShell
import me.arasple.mc.trhologram.api.base.BaseCondition
import me.arasple.mc.trhologram.module.service.Performance
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/10 11:02
 */
inline class Condition(private val expression: String) : BaseCondition {

    override fun eval(player: Player): Result {
        return if (expression.isEmpty()) Result.TRUE
        else eval(player, expression)
    }

    override fun toString(): String {
        return expression
    }

    companion object {

        fun eval(player: Player, script: String): Result {
            Performance.MIRROR.check("Hologram:Handler:ScriptEval") {
                return Result(KetherShell.eval(script, namespace = listOf("trhologram", "trmenu")) {
                    sender = player
                })
            }
            throw Exception()
        }

    }

}