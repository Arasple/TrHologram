package me.arasple.mc.trhologram.module.display.texture

import me.arasple.mc.trhologram.api.base.ItemTexture
import me.arasple.mc.trmenu.module.display.MenuSession
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2021/2/12 21:11
 */
class TrMenuTexture(texture: String) : ItemTexture {

    private val texture = me.arasple.mc.trmenu.module.display.texture.Texture.createTexture(texture)

    override fun generate(player: Player): ItemStack {
        return texture.generate(MenuSession.getSession(player))
    }


}