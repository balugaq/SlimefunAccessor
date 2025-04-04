package com.balugaq.slimefunaccessor.libraries.foreground;

import com.balugaq.slimefunaccessor.libraries.utils.Pager;
import lombok.Getter;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Foreground {
    @Getter
    private static final Map<Location, Pager<Location>> connections = new ConcurrentHashMap<>();

    public Foreground() {
    }

    public static Pager<Location> getConnection(@Nonnull Location identifier) {
        Pager<Location> existing = connections.get(identifier);
        if (existing == null) {
            existing = newPager();
            connections.put(identifier, existing);
        }

        return existing;
    }

    public static Pager<Location> newPager() {
        Pager<Location> pager = new Pager<>();
        pager.setContentPerPage(6 * 8);
        return pager;
    }

    public void connect(@Nonnull Location identifier, @Nonnull Location location) {
        getOrCreateConnection(identifier).add(location);
    }

    public void disconnect(@Nonnull Location identifier, @Nonnull Location location) {
        getOrCreateConnection(identifier).remove(location);
    }

    public boolean isConnected(@Nonnull Location identifier, @Nonnull Location location) {
        return getOrCreateConnection(identifier).contains(location);
    }

    public boolean isEmpty(@Nonnull Location identifier) {
        return getOrCreateConnection(identifier).isEmpty();
    }

    public int totalConnected(@Nonnull Location identifier) {
        return getOrCreateConnection(identifier).size();
    }

    public void destroy(@Nonnull Location identifier) {
        getOrCreateConnection(identifier).clear();
    }

    public Pager<Location> getOrCreateConnection(@Nonnull Location identifier) {
        return connections.computeIfAbsent(identifier, k -> newPager());
    }
}
