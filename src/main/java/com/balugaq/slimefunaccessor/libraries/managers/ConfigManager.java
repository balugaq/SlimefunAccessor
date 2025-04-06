package com.balugaq.slimefunaccessor.libraries.managers;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author balugaq
 */
@Getter
public class ConfigManager extends Manager {
    private final Map<String, Object> cachedValues = new HashMap<>();

    public ConfigManager(@Nonnull final JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {
        save();
    }

    @Nullable
    public String getString(@Nonnull final String path) {
        if (cachedValues.containsKey(path)) {
            return String.valueOf(cachedValues.get(path));
        }

        final String value = getPlugin().getConfig().getString(path);
        cachedValues.put(path, value);
        return value;
    }

    public boolean getBoolean(@Nonnull final String path) {
        if (cachedValues.containsKey(path)) {
            return Boolean.parseBoolean(String.valueOf(cachedValues.get(path)));
        }

        final boolean value = getPlugin().getConfig().getBoolean(path);
        cachedValues.put(path, value);
        return value;
    }

    public int getInt(@Nonnull final String path) {
        if (cachedValues.containsKey(path)) {
            return Integer.parseInt(String.valueOf(cachedValues.get(path)));
        }

        final int value = getPlugin().getConfig().getInt(path);
        cachedValues.put(path, value);
        return value;
    }

    public double getDouble(@Nonnull final String path) {
        if (cachedValues.containsKey(path)) {
            return Double.parseDouble(String.valueOf(cachedValues.get(path)));
        }

        final double value = getPlugin().getConfig().getDouble(path);
        cachedValues.put(path, value);
        return value;
    }

    public long getLong(@Nonnull final String path) {
        if (cachedValues.containsKey(path)) {
            return Long.parseLong(String.valueOf(cachedValues.get(path)));
        }

        final long value = getPlugin().getConfig().getLong(path);
        cachedValues.put(path, value);
        return value;
    }

    public void set(@Nonnull final String path, @Nullable final Object value) {
        getPlugin().getConfig().set(path, value);
        cachedValues.put(path, value);
        save();
    }

    public void save() {
        getPlugin().saveConfig();
    }

    public void reload() {
        getPlugin().reloadConfig();
        cachedValues.clear();
    }
}
