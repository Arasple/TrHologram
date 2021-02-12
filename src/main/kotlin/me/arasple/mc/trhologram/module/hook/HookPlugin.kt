package me.arasple.mc.trhologram.module.hook

import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2021/2/12 21:12
 */
object HookPlugin {

    val TRMENU by lazy {

        Bukkit.getPluginManager().getPlugin("TrMenu")?.isEnabled ?: false

    }

}