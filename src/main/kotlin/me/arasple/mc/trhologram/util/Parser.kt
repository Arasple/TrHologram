package me.arasple.mc.trhologram.util

import io.izzel.taboolib.kotlin.kether.KetherFunction
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.module.locale.chatcolor.TColor
import io.izzel.taboolib.util.Coerce
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2021/2/10 21:15
 */
object Parser {

    fun parseLocation(string: String): Location {
        val (world, loc) = string.split("~", limit = 2)
        val (x, y, z) = loc.split(",", limit = 3).map { it.toDouble() }

        return Location(Bukkit.getWorld(world), x, y, z)
    }

    fun fromLocation(location: Location): String {
        val world = location.world.name
        val x = Coerce.format(location.x)
        val y = Coerce.format(location.y)
        val z = Coerce.format(location.z)

        return "$world~$x,$y,$z"
    }

    fun parseString(player: Player, string: String): String {
        return TColor.translate(TLocale.Translate.setPlaceholders(player, KetherFunction.parse(string){ sender = player }))
    }

}