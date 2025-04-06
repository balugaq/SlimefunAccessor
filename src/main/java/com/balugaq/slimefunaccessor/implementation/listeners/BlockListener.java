package com.balugaq.slimefunaccessor.implementation.listeners;

import com.balugaq.slimefunaccessor.implementation.slimefun.Accessor;
import com.balugaq.slimefunaccessor.libraries.slimefun.foreground.AccessorForeground;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockBreakEvent;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author balugaq
 */
public class BlockListener implements Listener {
    @EventHandler
    public void onSlimefunBlockPlace(final SlimefunBlockPlaceEvent event) {
        if (!(event.getSlimefunItem() instanceof Accessor)) {
            AccessorForeground.addAccessible(event.getBlockPlaced().getLocation());
        }
    }

    @EventHandler
    public void onSlimefunBlockBreak(final SlimefunBlockBreakEvent event) {
        AccessorForeground.removeAccessible(event.getBlockBroken().getLocation());
    }
}
