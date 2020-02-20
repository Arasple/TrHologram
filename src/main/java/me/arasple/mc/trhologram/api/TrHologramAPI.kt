package me.arasple.mc.trhologram.api

import me.arasple.mc.trhologram.hologram.Hologram

/**
 * @author Arasple
 * @date 2020/2/20 17:49
 */
object TrHologramAPI {

    fun getHologramById(id: String): Hologram? {
        return Hologram.getHolograms().stream().filter { holo -> holo.id.equals(id, true) }.findFirst().orElse(null)
    }

    fun getHologramByEntityId(id: Int): Hologram? {
        return Hologram.getHolograms().stream().filter { holo -> holo.lines.stream().anyMatch { line -> line.id == id } }.findFirst().orElse(null)
    }

    fun getHologramIds(): List<String> {
        val list = mutableListOf<String>()
        Hologram.getHolograms().forEach { holo ->
            list.add(holo.id)
        }
        return list
    }


}