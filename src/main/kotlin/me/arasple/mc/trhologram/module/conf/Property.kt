package me.arasple.mc.trhologram.module.conf

/**
 * @author Arasple
 * @date 2021/2/11 17:26
 */
enum class Property(val regex: Regex, val group: Int) {

    OFFSET("\\{offset?[:=] ?([0-9.]+)}"),

    UPDATE("\\{(update|refresh)?[:=] ?([0-9]+)}", 2),

    ITEM("\\{items?[:=] ?(.+)}");

    constructor(regex: String, group: Int = 1) : this("(?i)$regex".toRegex(), group)

    companion object {

        fun from(string: String): Pair<String, Map<Property, String>> {
            var content = string
            val map = values().mapNotNull {
                val value = it.regex.find(content)?.groupValues?.get(it.group)
                value?.let { v ->
                    content = content.replace(it.regex, "")
                    it to v
                }
            }.toMap()

            return content to map
        }

    }

}