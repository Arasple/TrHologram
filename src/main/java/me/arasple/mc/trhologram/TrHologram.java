package me.arasple.mc.trhologram;

import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import me.arasple.mc.trhologram.hologram.Hologram;
import me.arasple.mc.trhologram.hologram.HologramManager;

/**
 * @author Arasple
 * @date 2020/1/1 16:57
 */
@Plugin.Version(5.15)
public final class TrHologram extends Plugin {

    @TInject("settings.yml")
    private static TConfig settings;
    @TInject("§2Tr§aHologram")
    private static TLogger logger;
    @TInject(state = TInject.State.LOADING, init = "init", active = "load", cancel = "unload")
    private static TrHologramLoader loader;

    @Override
    public void onStopping() {
        HologramManager.getHolograms().forEach(Hologram::destroyAll);
    }

    public static TConfig getSettings() {
        return settings;
    }

    public static TLogger getTLogger() {
        return logger;
    }

    public static double getTrVersion() {
        return 0.1;
    }

}
