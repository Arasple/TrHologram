package me.arasple.mc.trhologram;

import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import me.arasple.mc.trhologram.hologram.Hologram;
import me.arasple.mc.trhologram.hologram.HologramManager;
import me.arasple.mc.trhologram.utils.FileWatcher;

/**
 * @author Arasple
 * @date 2020/1/1 16:57
 */
@Plugin.Version(5.17)
public final class TrHologram extends Plugin {

    @TInject("settings.yml")
    public final static TConfig SETTINGS = null;
    @TInject("§5Tr§dHologram")
    public final static TLogger LOGGER = null;
    @TInject(state = TInject.State.LOADING, init = "init", active = "load", cancel = "unload")
    private final static TrHologramLoader LOADER = null;

    @Override
    public void onStopping() {
        HologramManager.getHolograms().forEach(Hologram::destroyAll);
        FileWatcher.getWatcher().unregisterAll();
    }

    public static double getTrVersion() {
        return 0.1;
    }

}
