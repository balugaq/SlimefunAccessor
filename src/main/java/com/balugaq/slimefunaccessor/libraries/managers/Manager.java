package com.balugaq.slimefunaccessor.libraries.managers;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 *
 * @author balugaq
 */
@Getter
public abstract class Manager {
    private final JavaPlugin plugin;

    public Manager(@Nonnull final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void load();

    public abstract void unload();
}
