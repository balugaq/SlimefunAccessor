package com.balugaq.slimefunaccessor.libraries.managers;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ConfigManager extends Manager {
    private final Map<String, Object> cachedValues = new HashMap<>();

    public ConfigManager(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Nullable
    public String getString(@NotNull String path) {
        if (cachedValues.containsKey(path)) {
            return String.valueOf(cachedValues.get(path));
        }

        String value = getPlugin().getConfig().getString(path);
        cachedValues.put(path, value);
        return value;
    }

    public boolean getBoolean(@NotNull String path) {
        if (cachedValues.containsKey(path)) {
            return Boolean.parseBoolean(String.valueOf(cachedValues.get(path)));
        }

        boolean value = getPlugin().getConfig().getBoolean(path);
        cachedValues.put(path, value);
        return value;
    }

    public int getInt(@NotNull String path) {
        if (cachedValues.containsKey(path)) {
            return Integer.parseInt(String.valueOf(cachedValues.get(path)));
        }

        int value = getPlugin().getConfig().getInt(path);
        cachedValues.put(path, value);
        return value;
    }

    public double getDouble(@NotNull String path) {
        if (cachedValues.containsKey(path)) {
            return Double.parseDouble(String.valueOf(cachedValues.get(path)));
        }

        double value = getPlugin().getConfig().getDouble(path);
        cachedValues.put(path, value);
        return value;
    }

    public long getLong(@NotNull String path) {
        if (cachedValues.containsKey(path)) {
            return Long.parseLong(String.valueOf(cachedValues.get(path)));
        }

        long value = getPlugin().getConfig().getLong(path);
        cachedValues.put(path, value);
        return value;
    }

    public void set(@NotNull String path, @Nullable Object value) {
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
