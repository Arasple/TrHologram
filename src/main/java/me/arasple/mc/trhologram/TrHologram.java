package me.arasple.mc.trhologram;

import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import me.arasple.mc.trhologram.hologram.HologramManager;
import me.arasple.mc.trhologram.utils.FileWatcher;

/**
 * @author Arasple
 * @date 2020/1/1 16:57
 */
@Plugin.Version(5.17)
public final class TrHologram extends Plugin {

    @TInject(value = "settings.yml", locale = "LOCALE")
    public final static TConfig SETTINGS = null;
    @TInject("§2Tr§aHologram")
    public final static TLogger LOGGER = null;
    @TInject(state = TInject.State.LOADING, init = "init", active = "active")
    private final static TrHologramLoader LOADER = null;

    @Override
    public void onStopping() {
        FileWatcher.INSTANCE.getWatcher().unregisterAll();
        HologramManager.INSTANCE.forceWrite();
        TLocale.sendToConsole("PLUGIN.DISABLED");
    }

    public static double getTrVersion() {
        return 1.1;
    }

}
