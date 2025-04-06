package com.balugaq.slimefunaccessor.libraries.foreground;

import com.balugaq.slimefunaccessor.libraries.utils.Pager;
import lombok.Getter;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author balugaq
 */
@Getter
public class Foreground {
    @Getter
    private static final Map<Location, Pager<Location>> connections = new ConcurrentHashMap<>();

    public Foreground() {
    }

    @Nonnull
    public static Pager<Location> getConnection(@Nonnull final Location identifier) {
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

    public void connect(@Nonnull final Location identifier, @Nonnull final Location location) {
        getOrCreateConnection(identifier).add(location);
    }

    public void disconnect(@Nonnull final Location identifier, @Nonnull final Location location) {
        getOrCreateConnection(identifier).remove(location);
    }

    public boolean isConnected(@Nonnull final Location identifier, @Nonnull final Location location) {
        return getOrCreateConnection(identifier).contains(location);
    }

    public boolean isEmpty(@Nonnull final Location identifier) {
        return getOrCreateConnection(identifier).isEmpty();
    }

    public int totalConnected(@Nonnull final Location identifier) {
        return getOrCreateConnection(identifier).size();
    }

    public void destroy(@Nonnull final Location identifier) {
        getOrCreateConnection(identifier).clear();
    }

    @Nonnull
    public Pager<Location> getOrCreateConnection(@Nonnull final Location identifier) {
        return connections.computeIfAbsent(identifier, k -> newPager());
    }
}
