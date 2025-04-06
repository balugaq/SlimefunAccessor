package com.balugaq.slimefunaccessor.libraries.slimefun;

import com.balugaq.slimefunaccessor.implementation.main.SlimefunAccessorPlugin;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 *
 * @author balugaq
 */
public class AccessorItem extends CustomSlimefunItem {
    public AccessorItem(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public AccessorItem(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    protected AccessorItem(final ItemGroup itemGroup, final ItemStack item, final String id, final RecipeType recipeType, final ItemStack[] recipe) {
        super(itemGroup, item, id, recipeType, recipe);
    }

    public AccessorItem registerItem() {
        super.register(SlimefunAccessorPlugin.instance());
        return this;
    }
}
