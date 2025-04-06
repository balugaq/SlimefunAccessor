package com.balugaq.slimefunaccessor.libraries.utils;

import com.balugaq.slimefunaccessor.implementation.main.SlimefunAccessorPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author balugaq
 */
public class KeyUtils {
    private KeyUtils() {

    }

    public static NamespacedKey newKey(final String key) {
        return newKey(SlimefunAccessorPlugin.instance(), key);
    }

    public static NamespacedKey newKey(final Plugin namespace, final String key) {
        return new NamespacedKey(namespace, key);
    }

    public static NamespacedKey newKey(final String namespace, final String key) {
        return new NamespacedKey(namespace, key);
    }
}
