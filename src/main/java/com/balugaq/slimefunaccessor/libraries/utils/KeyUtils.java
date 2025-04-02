package com.balugaq.slimefunaccessor.libraries.utils;

import com.balugaq.slimefunaccessor.implementation.main.SlimefunAccessor;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class KeyUtils {
    private KeyUtils() {

    }

    public static NamespacedKey newKey(String key) {
        return newKey(SlimefunAccessor.instance(), key);
    }

    public static NamespacedKey newKey(Plugin namespace, String key) {
        return new NamespacedKey(namespace, key);
    }

    public static NamespacedKey newKey(String namespace, String key) {
        return new NamespacedKey(namespace, key);
    }
}
