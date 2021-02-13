package me.arasple.mc.trhologram.module.hook

import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2021/2/12 21:12
 */
object HookPlugin {

    val TRMENU by lazy {

        val plugin = Bukkit.getPluginManager().getPlugin("TrMenu")

        plugin != null && plugin.isEnabled && plugin.description.version.startsWith("3")

    }

}