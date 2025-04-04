package com.balugaq.slimefunaccessor.libraries.slimefun;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MenuItem extends AccessorItem {
    public MenuItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        setupMenuPreset();
    }

    public MenuItem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
        setupMenuPreset();
    }

    protected MenuItem(ItemGroup itemGroup, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, id, recipeType, recipe);
        setupMenuPreset();
    }

    public void setupMenuPreset() {
        new BlockMenuPreset(getId(), getItemName()) {
            @Override
            public void init() {
                MenuItem.this.init(this);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu blockMenu, @Nonnull Block block) {
                MenuItem.this.newInstance(blockMenu, block);
            }

            @Override
            public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
                return MenuItem.this.canOpen(block, player);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return MenuItem.this.getSlotsAccessedByItemTransport(itemTransportFlow);
            }
        };
    }

    public abstract void init(@Nonnull BlockMenuPreset preset);

    public abstract void newInstance(@Nonnull BlockMenu blockMenu, @Nonnull Block block);

    public boolean canOpenDefaultImpl(@Nonnull Block block, @Nonnull Player player) {
        return player.hasPermission("slimefun.inventory.bypass") || player.isOp() || Slimefun.getProtectionManager().hasPermission(player, block, Interaction.INTERACT_BLOCK);
    }

    public boolean canOpen(@Nonnull Block block, @Nonnull Player player) {
        return canOpenDefaultImpl(block, player);
    }

    public abstract int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow);

    public BlockMenuPreset getMenuPreset() {
        return Slimefun.getRegistry().getMenuPresets().get(getId());
    }
}
