package com.balugaq.slimefunaccessor.implementation.slimefun;

import com.balugaq.slimefunaccessor.libraries.slimefun.MenuItem;
import com.balugaq.slimefunaccessor.libraries.slimefun.foreground.AccessorForeground;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class Accessor extends MenuItem {
    public static final String BS_FILTER_KEY = "filter";
    public Accessor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        AccessorForeground.applyBlockMenuPreset(getMenuPreset());
    }

    @Override
    public void init(@Nonnull BlockMenuPreset preset) {

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
