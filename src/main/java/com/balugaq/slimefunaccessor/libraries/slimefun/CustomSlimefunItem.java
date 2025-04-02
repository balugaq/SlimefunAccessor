package com.balugaq.slimefunaccessor.libraries.slimefun;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class CustomSlimefunItem extends SlimefunItem {
    public CustomSlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public CustomSlimefunItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    protected CustomSlimefunItem(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, id, recipeType, recipe);
    }

    public CustomSlimefunItem registerItem(SlimefunAddon plugin) {
        super.register(plugin);
        return this;
    }
}
