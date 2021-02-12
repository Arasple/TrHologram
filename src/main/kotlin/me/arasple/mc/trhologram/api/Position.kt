package me.arasple.mc.trhologram.api

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import kotlin.math.pow

/**
 * @author Arasple
 * @date 2021/2/10 10:53
 */
class Position(
    val world: World = Bukkit.getWorlds()[0],
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0
) {

    fun distance(position: Position): Double {
        return distance(position.x, position.y, position.z)
    }

    fun distance(location: Location): Double {
        return distance(location.x, location.y, location.z)
    }

    fun clone(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Position {
        return Position(world, this.x + x, this.y + y, this.z + z)
    }

    private fun distance(x: Double, y: Double, z: Double): Double {
        return Math.sqrt((this.x - x).pow(2) + (this.y - y).pow(2) + (this.z - z).pow(2))
    }

    fun toLocation(): Location {
        return Location(world, x, y, z)
    }

    override fun toString(): String {
        return "${world.name} ~ $x, $y, $z"
    }

    companion object {

        fun fromLocation(location: Location): Position {
            return Position(location.world!!, location.x, location.y, location.z)
        }

    }

}