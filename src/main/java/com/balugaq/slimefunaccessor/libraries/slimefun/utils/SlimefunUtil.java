package com.balugaq.slimefunaccessor.libraries.slimefun.utils;

import com.balugaq.slimefunaccessor.implementation.main.SlimefunAccessorPlugin;
import com.balugaq.slimefunaccessor.libraries.utils.ItemStackUtil;
import com.balugaq.slimefunaccessor.libraries.utils.PdcUtil;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author balugaq
 */
public class SlimefunUtil {
    private SlimefunUtil() {
    }

    @ParametersAreNonnullByDefault
    public static ItemStack safeCopy(final ItemStack itemStack, final Location location, final String id) {
        final ItemStack copy = ItemStackUtil.safeCopy(itemStack);
        PdcUtil.setLocationPdc(copy, location);
        PdcUtil.setMirrorSlimefunIdPdc(copy, id);
        return copy;
    }

    public static void unregisterAll() {
        unregisterAllItems();
        unregisterItemGroups();
    }

    public static void unregisterItem(@Nonnull final SlimefunItem item) {
        if (item instanceof Radioactive) {
            Slimefun.getRegistry().getRadioactiveItems().remove(item);
        }

        if (item instanceof GEOResource geor) {
            Slimefun.getRegistry().getGEOResources().remove(geor.getKey());
        }

        Slimefun.getRegistry().getTickerBlocks().remove(item.getId());
        Slimefun.getRegistry().getEnabledSlimefunItems().remove(item);

        Slimefun.getRegistry().getSlimefunItemIds().remove(item.getId());
        Slimefun.getRegistry().getAllSlimefunItems().remove(item);
        Slimefun.getRegistry().getMenuPresets().remove(item.getId());
        Slimefun.getRegistry().getBarteringDrops().remove(item.getItem());
    }

    public static void unregisterAllItems() {
        final List<SlimefunItem> items = new ArrayList<>(Slimefun.getRegistry().getAllSlimefunItems());
        for (SlimefunItem item : items) {
            if (item.getAddon() instanceof SlimefunAccessorPlugin) {
                unregisterItem(item);
            }
        }
    }

    public static void unregisterItemGroups() {
        final Set<ItemGroup> itemGroups = new HashSet<>();
        for (final ItemGroup itemGroup : Slimefun.getRegistry().getAllItemGroups()) {
            if (itemGroup.getAddon() instanceof SlimefunAccessorPlugin) {
                itemGroups.add(itemGroup);
            }
        }
        for (final ItemGroup itemGroup : itemGroups) {
            unregisterItemGroup(itemGroup);
        }
    }

    public static void unregisterItemGroup(@Nullable final ItemGroup itemGroup) {
        if (itemGroup == null) {
            return;
        }

        Slimefun.getRegistry().getAllItemGroups().remove(itemGroup);
    }
}
