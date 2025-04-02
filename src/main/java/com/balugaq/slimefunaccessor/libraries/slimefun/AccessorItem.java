package com.balugaq.slimefunaccessor.libraries.slimefun;

import com.balugaq.slimefunaccessor.implementation.main.SlimefunAccessor;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class AccessorItem extends CustomSlimefunItem {
    public AccessorItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public AccessorItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    protected AccessorItem(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, id, recipeType, recipe);
    }

    public AccessorItem registerItem() {
        super.register(SlimefunAccessor.instance());
        return this;
    }
}
