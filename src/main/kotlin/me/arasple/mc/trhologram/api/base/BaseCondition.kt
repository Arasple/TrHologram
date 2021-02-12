package me.arasple.mc.trhologram.api.base

import me.arasple.mc.trhologram.module.condition.EvalResult
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/12 14:08
 */
fun interface BaseCondition {

    fun eval(player: Player): EvalResult

}