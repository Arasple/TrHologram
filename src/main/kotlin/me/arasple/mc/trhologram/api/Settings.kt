package me.arasple.mc.trhologram.api

import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject
import io.izzel.taboolib.util.lite.cooldown.Cooldown

/**
 * @author Arasple
 * @date 2021/2/11 10:07
 */
class Settings {

    companion object {

        @TInject("settings.yml", locale = "Options.Language", migrate = true)
        lateinit var CONF: TConfig
            private set

        internal var INSTANCE = Settings()

        fun init() {
            CONF.listener { onSettingsReload() }.also { onSettingsReload() }
        }

        fun onSettingsReload() {
            INSTANCE = Settings()
        }

    }

    val loadPaths: List<String> by lazy {
        CONF.getStringList("Loader.Hologram-Files")
    }

    val interactDelay by lazy {
        Cooldown("TrHologram", CONF.getLong("Hologram.Interact-Min-Delay")).also {
            it.plugin = "TrHologram"
        }
    }

    val lineSpacing by lazy {
        CONF.getDouble("Hologram.Options.Default-Line-Spacing", 0.25)
    }

    val viewDistance by lazy {
        CONF.getDouble("Hologram.Options.Default-View-Distance", 20.0)
    }

    val viewCondition by lazy {
        CONF.getString("Hologram.Options.Default-View-Condition", "")
    }

    val refershCondition by lazy {
        CONF.getLong("Hologram.Options.Default-Refresh-Condition", -1)
    }

}