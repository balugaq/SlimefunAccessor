package com.balugaq.slimefunaccessor.implementation.main;

import com.balugaq.slimefunaccessor.implementation.slimefun.Accessor;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class AccessorItems {
    private AccessorItems() {
    }

    public static Accessor ACCESSOR;

    public static void setup() {
        ACCESSOR = (Accessor) new Accessor(
                AccessorItemGroups.MACHINES,
                AccessorIcons.ACCESSOR,
                RecipeType.ENHANCED_CRAFTING_TABLE,
                AccessorRecipes.ACCESSOR
        ).registerItem();
    }
}
