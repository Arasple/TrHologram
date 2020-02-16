package me.arasple.mc.trhologram;

import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.dependency.Dependency;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;

/**
 * @author Arasple
 * @date 2020/1/1 16:57
 */
@Plugin.Version(5.17)
@Dependency(maven = "org:kotlinlang:kotlin-stdlib:1.3.50", url = "https://skymc.oss-cn-shanghai.aliyuncs.com/libs/kotlin-stdlib-1.3.50.jar")
public final class TrHologram extends Plugin {

    @TInject("settings.yml")
    public final static TConfig SETTINGS = null;
    @TInject("§2Tr§aHologram")
    public final static TLogger LOGGER = null;
    @TInject(state = TInject.State.LOADING, init = "init", active = "active", cancel = "cancel")
    private final static TrHologramLoader LOADER = null;

    public static double getTrVersion() {
        return 1.0;
    }

}
