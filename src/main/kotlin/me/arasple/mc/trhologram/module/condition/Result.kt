package me.arasple.mc.trhologram.module.condition

import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/1/31 11:53
 */
inline class Result(private val any: Any?) {

    fun asBoolean(def: Boolean = false): Boolean {
        return when (any) {
            is Boolean -> any
            is String -> any.parseBoolean()
            else -> def || any.toString().parseBoolean()
        }
    }

    fun asItemStack(): ItemStack? {
        return any as ItemStack?
    }

    fun asString(): String {
        return any.toString()
    }

    companion object {

        val TRUE = Result(true)
        private val REGEX_TRUE = "true|yes|on".toRegex()

        fun String.parseBoolean(): Boolean {
            return toLowerCase().matches(REGEX_TRUE)
        }

    }

}