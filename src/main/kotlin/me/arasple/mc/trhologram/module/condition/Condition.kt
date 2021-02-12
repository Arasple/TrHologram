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

    override fun eval(player: Player): EvalResult {
        return if (expression.isEmpty()) EvalResult.TRUE
        else eval(player, expression)
    }

    override fun toString(): String {
        return expression
    }

    companion object {

        fun eval(player: Player, script: String): EvalResult {
            Performance.MIRROR.check("Script:evalCondition") {
                return EvalResult(KetherShell.eval(script, namespace = listOf("trhologram", "trmenu")) {
                    this.sender = player
                })
            }
            throw Exception()
        }

    }

}