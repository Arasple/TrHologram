package me.arasple.mc.trhologram.module.condition

import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/1/31 11:53
 */
inline class Result(val raw: Any?) {

    fun asBoolean(def: Boolean = false): Boolean {
        return when (raw) {
            is Boolean -> raw
            is String -> raw.parseBoolean()
            else -> def || raw.toString().parseBoolean()
        }
    }

    fun asItemStack(): ItemStack? {
        return raw as ItemStack?
    }

    fun asString(): String {
        return raw.toString()
    }

    companion object {

        val TRUE = Result(true)
        private val REGEX_TRUE = "true|yes|on".toRegex()

        fun String.parseBoolean(): Boolean {
            return toLowerCase().matches(REGEX_TRUE)
        }

    }

}