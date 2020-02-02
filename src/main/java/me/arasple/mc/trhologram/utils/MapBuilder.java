package me.arasple.mc.trhologram.utils;

import java.util.HashMap;

/**
 * @author Arasple
 * @date 2020/2/2 13:34
 */
public class MapBuilder {

    private HashMap<String, Object> map;

    public MapBuilder() {
        this.map = new HashMap<>();
    }

    public MapBuilder put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public HashMap<String, Object> build() {
        return this.map;
    }

}
