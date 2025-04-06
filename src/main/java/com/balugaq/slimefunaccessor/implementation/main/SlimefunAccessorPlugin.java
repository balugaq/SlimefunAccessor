package com.balugaq.slimefunaccessor.implementation.main;

import com.balugaq.slimefunaccessor.implementation.listeners.BlockListener;
import com.balugaq.slimefunaccessor.implementation.listeners.InventoryListener;
import com.balugaq.slimefunaccessor.libraries.managers.ConfigManager;
import com.balugaq.slimefunaccessor.libraries.managers.IntegrationManager;
import com.balugaq.slimefunaccessor.libraries.managers.ListenerManager;
import com.balugaq.slimefunaccessor.libraries.slimefun.utils.SlimefunUtil;
import com.balugaq.slimefunaccessor.libraries.utils.Logger;
import com.google.common.base.Preconditions;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author balugaq
 */
@Getter
public class SlimefunAccessorPlugin extends JavaPlugin implements SlimefunAddon {
    private static SlimefunAccessorPlugin instance;
    private ConfigManager configManager;
    private ListenerManager listenerManager;
    private IntegrationManager integrationManager;

    @Nonnull
    public static SlimefunAccessorPlugin instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Preconditions.checkState(instance == null, "SlimefunAccessor is already enabled!");
        instance = this;
        configManager = new ConfigManager(this);
        configManager.load();
        Logger.setDebug(configManager.getBoolean("debug"));
        listenerManager = new ListenerManager(this);
        listenerManager.addListener(new BlockListener());
        listenerManager.addListener(new InventoryListener());
        listenerManager.load();

        integrationManager = new IntegrationManager(this);
        integrationManager.load();

        AccessorItemGroups.setup();
        AccessorItems.setup();
    }

    @Override
    public void onDisable() {
        Preconditions.checkState(instance == this, "SlimefunAccessor is not enabled!");

        SlimefunUtil.unregisterAll();

        integrationManager.unload();
        listenerManager.unload();
        configManager.unload();
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
