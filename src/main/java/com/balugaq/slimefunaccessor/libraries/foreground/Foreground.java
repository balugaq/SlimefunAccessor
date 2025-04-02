package com.balugaq.slimefunaccessor.libraries.foreground;

import com.balugaq.slimefunaccessor.libraries.utils.Pager;
import lombok.Getter;
import org.bukkit.Location;

import javax.annotation.Nonnull;

@Getter
public class Foreground {
    public final Pager<Location> connection;

    public Foreground() {
        this.connection = new Pager<>();
    }

    public void connect(@Nonnull Location location) {
        this.connection.add(location);
    }

    public void disconnect(@Nonnull Location location) {
        this.connection.remove(location);
    }

    public boolean isConnected(@Nonnull Location location) {
        return this.connection.contains(location);
    }

    public boolean isEmpty() {
        return this.connection.isEmpty();
    }

    public int totalConnected() {
        return this.connection.size();
    }

    public void destroy() {
        this.connection.clear();
    }
}
