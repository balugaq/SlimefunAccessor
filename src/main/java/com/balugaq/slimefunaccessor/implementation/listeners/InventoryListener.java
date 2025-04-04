package com.balugaq.slimefunaccessor.implementation.listeners;

import com.balugaq.slimefunaccessor.implementation.main.SlimefunAccessor;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryListener implements Listener {
    private static final Map<UUID, Location> remoteAccessingPlayers = new HashMap<>();
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID uuid = player.getUniqueId();
        Location location = remoteAccessingPlayers.get(uuid);
        if (location != null) {
            remoteAccessingPlayers.remove(uuid);
            BlockMenu menu = StorageCacheUtils.getMenu(location);
            if (menu != null) {
                Bukkit.getScheduler().runTaskLater(SlimefunAccessor.instance(), () -> {
                    menu.open(player);
                }, 1L);
            }
        }
    }

    public static void addRemoteAccessingPlayer(Player player, Location location) {
        remoteAccessingPlayers.put(player.getUniqueId(), location);
    }
}
