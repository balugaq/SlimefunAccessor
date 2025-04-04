package com.balugaq.slimefunaccessor.libraries.managers;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ListenerManager extends Manager {
    private final List<Listener> listeners = new ArrayList<>();

    public ListenerManager(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        for (Listener listener : listeners) {
            getPlugin().getServer().getPluginManager().registerEvents(listener, getPlugin());
        }
    }

    @Override
    public void unload() {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
        listeners.clear();
    }

    public void addListener(@Nonnull Listener listener) {
        listeners.add(listener);
    }
}
