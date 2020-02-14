package me.arasple.mc.trhologram.utils;

import io.izzel.taboolib.module.config.TConfigWatcher;

/**
 * @author Arasple
 * @date 2020/2/14 21:04
 */
public class FileWatcher {

    private static TConfigWatcher watcher;

    public static TConfigWatcher getWatcher() {
        if (watcher == null) {
            watcher = new TConfigWatcher();
        }
        return watcher;
    }

}
