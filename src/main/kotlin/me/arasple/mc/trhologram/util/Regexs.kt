package me.arasple.mc.trhologram.util

/**
 * @author Arasple
 * @date 2021/1/24 16:57
 */
object Regexs {

    private val PLACEHOLDER_API = "[%{](.+?)[%}]".toRegex()
    private val TRUE = "true|yes|on".toRegex()

    fun containsPlaceholder(string: String): Boolean {
        return PLACEHOLDER_API.find(string) != null
    }

}