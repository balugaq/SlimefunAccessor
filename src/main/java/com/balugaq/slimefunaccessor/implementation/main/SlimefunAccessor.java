package com.balugaq.slimefunaccessor.implementation.main;

import com.google.common.base.Preconditions;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlimefunAccessor extends JavaPlugin implements SlimefunAddon {
    public static SlimefunAccessor instance;

    @Nonnull
    public static SlimefunAccessor instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Preconditions.checkState(instance == null, "SlimefunAccessor is already enabled!");
        instance = this;

        AccessorItemGroups.setup();
        AccessorItems.setup();
    }

    @Override
    public void onDisable() {
        Preconditions.checkState(instance == this, "SlimefunAccessor is not enabled!");
        instance = null;
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return "https://github.com/balugaq/SlimefunAccessor/issues";
    }
}
