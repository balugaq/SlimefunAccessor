package com.balugaq.slimefunaccessor.implementation.main;

import com.balugaq.slimefunaccessor.implementation.listeners.BlockListener;
import com.balugaq.slimefunaccessor.libraries.managers.ConfigManager;
import com.balugaq.slimefunaccessor.libraries.managers.ListenerManager;
import com.balugaq.slimefunaccessor.libraries.utils.Logger;
import com.google.common.base.Preconditions;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Getter
public class SlimefunAccessor extends JavaPlugin implements SlimefunAddon {
    private static SlimefunAccessor instance;
    private ConfigManager configManager;
    private ListenerManager listenerManager;

    @Nonnull
    public static SlimefunAccessor instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Preconditions.checkState(instance == null, "SlimefunAccessor is already enabled!");
        instance = this;
        configManager = new ConfigManager(this);
        Logger.setDebug(configManager.getBoolean("debug"));
        listenerManager = new ListenerManager(this);
        listenerManager.addListener(new BlockListener());
        listenerManager.load();

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
