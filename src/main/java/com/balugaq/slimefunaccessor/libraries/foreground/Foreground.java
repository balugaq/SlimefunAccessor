package com.balugaq.slimefunaccessor.libraries.foreground;

import com.balugaq.slimefunaccessor.libraries.utils.Pager;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Foreground {
    public static final Map<Location, Pager<Location>> connection = new ConcurrentHashMap<>();

    public Foreground() {
    }

    public static Pager<Location> getConnection(@Nonnull Location identifier) {
        return connection.getOrDefault(identifier, newPager());
    }

    public void connect(@Nonnull Location identifier, @Nonnull Location location) {
        connection.compute(identifier, (k, v) -> v == null? newPager() : v).add(identifier, location);
    }

    public void disconnect(@Nonnull Location identifier, @Nonnull Location location) {
        connection.getOrDefault(identifier, newPager()).remove(location);
    }

    public boolean isConnected(@Nonnull Location identifier, @Nonnull Location location) {
        return connection.getOrDefault(identifier, newPager()).contains(location);
    }

    public boolean isEmpty(@Nonnull Location identifier) {
        return connection.getOrDefault(identifier, newPager()).isEmpty();
    }

    public int totalConnected(@Nonnull Location identifier) {
        return connection.getOrDefault(identifier, newPager()).size();
    }

    public void destroy(@Nonnull Location identifier) {
        connection.getOrDefault(identifier, newPager()).clear();
    }

    public static Pager<Location> newPager() {
        Pager<Location> pager = new Pager<>();
        pager.setContentPerPage(6 * 8);
        return pager;
    }
}
