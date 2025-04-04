package com.balugaq.slimefunaccessor.implementation.slimefun;

import com.balugaq.slimefunaccessor.libraries.slimefun.MenuItem;
import com.balugaq.slimefunaccessor.libraries.slimefun.foreground.AccessorForeground;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Accessor extends MenuItem {
    public static final Set<Location> AUTO_RELATED = new HashSet<>();
    public static final int DEFAULT_RANGE = 100;
    public static final String BS_FILTER_KEY = "filter";
    public static final String BS_RANGE_KEY = "range";
    private static final Set<Location> LOADED = new HashSet<>();

    public Accessor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        AccessorForeground.applyBlockMenuPreset(getMenuPreset());
        addItemHandler(
                new BlockPlaceHandler(false) {
                    @Override
                    public void onPlayerPlace(@Nonnull BlockPlaceEvent blockPlaceEvent) {
                        Location location = blockPlaceEvent.getBlock().getLocation();
                        load(location, AccessorForeground.getConnection(location), DEFAULT_RANGE);
                    }
                },
                new BlockBreakHandler(false, false) {
                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerBreak(BlockBreakEvent blockBreakEvent, ItemStack itemStack, List<ItemStack> list) {
                        Location location = blockBreakEvent.getBlock().getLocation();
                        unload(location);
                    }
                },
                new BlockTicker() {
                    @Override
                    public boolean isSynchronized() {
                        return false;
                    }

                    @Override
                    public void tick(Block block, SlimefunItem slimefunItem, SlimefunBlockData blockData) {
                        Location location = block.getLocation();
                        if (LOADED.contains(location)) {
                            AccessorForeground.Behavior.UPDATE_MENU.apply(
                                    AccessorForeground.getConnection(location),
                                    StorageCacheUtils.getMenu(location),
                                    null,
                                    null,
                                    null,
                                    null
                            );
                        } else {
                            // first load
                            String rangeStr = StorageCacheUtils.getData(location, Accessor.BS_RANGE_KEY);
                            int range = rangeStr == null ? DEFAULT_RANGE : Integer.parseInt(rangeStr);
                            load(location, AccessorForeground.getConnection(location), range);
                            AccessorForeground.Behavior.UPDATE_MENU.apply(
                                    AccessorForeground.getConnection(location),
                                    StorageCacheUtils.getMenu(location),
                                    null,
                                    null,
                                    null,
                                    null
                            );
                        }
                    }
                });
    }

    public static void load(Location root, Pager<Location> connection, int radius) {
        LOADED.add(root);
        StorageCacheUtils.setData(root, Accessor.BS_RANGE_KEY, String.valueOf(radius));
        World world = root.getWorld();
        new HashMap<>(Slimefun.getTickerTask().getLocations()).values().forEach(locations -> {
            locations.forEach(location -> {
                if (location.getWorld() == world && location.distance(root) <= radius) {
                    if (!(StorageCacheUtils.getSfItem(location) instanceof Accessor)) {
                        connection.add(location);
                    }
                }
            });
        });
        connection.setDirty(true);
    }

    public static void unload(Location root) {
        LOADED.remove(root);
        AUTO_RELATED.remove(root);
        AccessorForeground.getConnection(root).clear();
        AccessorForeground.getConnections().remove(root);
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
