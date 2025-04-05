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

public class SlimefunUtil {
    private SlimefunUtil() {
    }

    @ParametersAreNonnullByDefault
    public static ItemStack safeCopy(ItemStack itemStack, Location location, String id) {
        ItemStack copy = ItemStackUtil.safeCopy(itemStack);
        PdcUtil.setLocationPdc(copy, location);
        PdcUtil.setMirrorSlimefunIdPdc(copy, id);
        return copy;
    }

    public static void unregisterAll() {
        unregisterAllItems();
        unregisterItemGroups();
    }

    public static void unregisterItem(@Nonnull SlimefunItem item) {
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
        List<SlimefunItem> items = new ArrayList<>(Slimefun.getRegistry().getAllSlimefunItems());
        for (SlimefunItem item : items) {
            if (item.getAddon() instanceof SlimefunAccessorPlugin) {
                unregisterItem(item);
            }
        }
    }

    public static void unregisterItemGroups() {
        Set<ItemGroup> itemGroups = new HashSet<>();
        for (ItemGroup itemGroup : Slimefun.getRegistry().getAllItemGroups()) {
            if (itemGroup.getAddon() instanceof SlimefunAccessorPlugin) {
                itemGroups.add(itemGroup);
            }
        }
        for (ItemGroup itemGroup : itemGroups) {
            unregisterItemGroup(itemGroup);
        }
    }

    public static void unregisterItemGroup(@Nullable ItemGroup itemGroup) {
        if (itemGroup == null) {
            return;
        }

        Slimefun.getRegistry().getAllItemGroups().remove(itemGroup);
    }
}
