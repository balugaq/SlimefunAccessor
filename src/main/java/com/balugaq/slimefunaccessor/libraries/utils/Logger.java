package com.balugaq.slimefunaccessor.libraries.utils;

import com.balugaq.slimefunaccessor.implementation.main.SlimefunAccessor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Level;

public class Logger {
    public static final String DEBUG_PREFIX = "[DEBUG] ";

    private Logger() {
    }

    @Nonnull
    public static java.util.logging.Logger getLogger(@Nullable JavaPlugin plugin) {
        if (plugin == null) {
            return java.util.logging.Logger.getLogger("SlimefunAccessor");
        }
        return plugin.getLogger();
    }

    @Nonnull
    public static java.util.logging.Logger getDefaultLogger() {
        return getLogger(SlimefunAccessor.instance());
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
        return SlimefunAccessor.instance().getConfig().getBoolean("debug");
    }
}
