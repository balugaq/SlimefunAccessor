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
        return connections.getOrDefault(identifier, newPager());
    }

    public static Pager<Location> newPager() {
        Pager<Location> pager = new Pager<>();
        pager.setContentPerPage(6 * 8);
        return pager;
    }

    public void connect(@Nonnull Location identifier, @Nonnull Location location) {
        connections.compute(identifier, (k, v) -> v == null ? newPager() : v).add(location);
    }

    public void disconnect(@Nonnull Location identifier, @Nonnull Location location) {
        connections.getOrDefault(identifier, newPager()).remove(location);
    }

    public boolean isConnected(@Nonnull Location identifier, @Nonnull Location location) {
        return connections.getOrDefault(identifier, newPager()).contains(location);
    }

    public boolean isEmpty(@Nonnull Location identifier) {
        return connections.getOrDefault(identifier, newPager()).isEmpty();
    }

    public int totalConnected(@Nonnull Location identifier) {
        return connections.getOrDefault(identifier, newPager()).size();
    }

    public void destroy(@Nonnull Location identifier) {
        connections.getOrDefault(identifier, newPager()).clear();
    }
}
