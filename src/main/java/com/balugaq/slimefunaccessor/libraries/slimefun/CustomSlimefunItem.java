package com.balugaq.slimefunaccessor.libraries.slimefun;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 *
 * @author balugaq
 */
public class CustomSlimefunItem extends SlimefunItem {
    public CustomSlimefunItem(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public CustomSlimefunItem(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    protected CustomSlimefunItem(final ItemGroup itemGroup, final ItemStack item, String id, final RecipeType recipeType, final ItemStack[] recipe) {
        super(itemGroup, item, id, recipeType, recipe);
    }

    public CustomSlimefunItem registerItem(final SlimefunAddon plugin) {
        super.register(plugin);
        return this;
    }
}
