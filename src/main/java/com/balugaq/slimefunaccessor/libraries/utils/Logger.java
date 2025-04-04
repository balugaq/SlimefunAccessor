package com.balugaq.slimefunaccessor.libraries.utils;

import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Level;

public class Logger {
    public static final String DEBUG_PREFIX = "[DEBUG] ";
    private static final String DEFAULT_PREFIX = "[SlimefunAccessor] ";
    @Setter
    public static boolean debug = false;
    private static JavaPlugin plugin;

    private Logger() {
    }

    @Nonnull
    public static java.util.logging.Logger getLogger(@Nullable JavaPlugin plugin) {
        if (plugin == null) {
            return java.util.logging.Logger.getLogger(DEFAULT_PREFIX);
        }
        return plugin.getLogger();
    }

    @Nonnull
    public static java.util.logging.Logger getDefaultLogger() {
        return getLogger(plugin);
    }

    public static void log(Object message) {
        getDefaultLogger().info(String.valueOf(message));
    }

    public static void warn(Object message) {
        getDefaultLogger().warning(String.valueOf(message));
    }

    public static void severe(Object message) {
        getDefaultLogger().severe(String.valueOf(message));
    }

    public static void debug(Object message) {
        if (debugging()) {
            getDefaultLogger().info(DEBUG_PREFIX + message);
        }
    }

    public static void trace(Throwable e) {
        getDefaultLogger().log(Level.SEVERE, "Exception occurred", e);
    }

    public static boolean debugging() {
        return debug;
    }
}
