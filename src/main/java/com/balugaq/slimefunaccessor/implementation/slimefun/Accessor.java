package com.balugaq.slimefunaccessor.implementation.slimefun;

import com.balugaq.slimefunaccessor.libraries.slimefun.MenuItem;
import com.balugaq.slimefunaccessor.libraries.slimefun.foreground.AccessorForeground;
import com.balugaq.slimefunaccessor.libraries.utils.Logger;
import com.balugaq.slimefunaccessor.libraries.utils.Pager;
import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;

public class Accessor extends MenuItem {
    public static final int RADIUS = 100;
    public static final String BS_FILTER_KEY = "filter";

    public Accessor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        AccessorForeground.applyBlockMenuPreset(getMenuPreset());
        addItemHandler(
                new BlockPlaceHandler(false) {
                    @Override
                    public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {
                        Location location = blockPlaceEvent.getBlock().getLocation();
                        load(location, AccessorForeground.getConnection(location), RADIUS);
                    }
                },
                new BlockBreakHandler(false, false) {
                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerBreak(BlockBreakEvent blockBreakEvent, ItemStack itemStack, List<ItemStack> list) {
                    }
                },
                new BlockTicker() {
                    @Override
                    public boolean isSynchronized() {
                        return false;
                    }

                    @Override
                    public void tick(Block block, SlimefunItem slimefunItem, SlimefunBlockData blockData) {
                        AccessorForeground.Behavior.UPDATE_MENU.apply(
                                AccessorForeground.getConnection(block.getLocation()),
                                StorageCacheUtils.getMenu(block.getLocation()),
                                null,
                                null,
                                null,
                                null
                        );
                    }
                });
    }

    public static void load(Location root, Pager<Location> connection, int radius) {
        Logger.log("Loading connection for " + root);
        World world = root.getWorld();
        new HashMap<>(Slimefun.getTickerTask().getLocations()).values().forEach(locations -> {
            locations.forEach(location -> {
                if (location.getWorld() == world && location.distance(root) <= radius) {
                    connection.add(location);
                }
            });
        });
    }

    @Override
    public void init(@Nonnull BlockMenuPreset preset) {
        preset.setSize(54);
    }

    @Override
    public void newInstance(@Nonnull BlockMenu blockMenu, @Nonnull Block block) {
        AccessorForeground.applyBlockMenu(blockMenu);
    }

    @Override
    public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
        return new int[0];
    }
}
