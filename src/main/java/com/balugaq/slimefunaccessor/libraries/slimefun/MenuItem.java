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

/**
 *
 * @author balugaq
 */
public abstract class MenuItem extends AccessorItem {
    public MenuItem(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        setupMenuPreset();
    }

    public MenuItem(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, @Nullable ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
        setupMenuPreset();
    }

    protected MenuItem(final ItemGroup itemGroup, final ItemStack item, final String id, final RecipeType recipeType, final ItemStack[] recipe) {
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
            public void newInstance(@Nonnull final BlockMenu blockMenu, @Nonnull final Block block) {
                MenuItem.this.newInstance(blockMenu, block);
            }

            @Override
            public boolean canOpen(@Nonnull final Block block, @Nonnull final Player player) {
                return MenuItem.this.canOpen(block, player);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(final ItemTransportFlow itemTransportFlow) {
                return MenuItem.this.getSlotsAccessedByItemTransport(itemTransportFlow);
            }
        };
    }

    public abstract void init(@Nonnull final BlockMenuPreset preset);

    public abstract void newInstance(@Nonnull final BlockMenu blockMenu, @Nonnull final Block block);

    public boolean canOpenDefaultImpl(@Nonnull final Block block, @Nonnull final Player player) {
        return player.hasPermission("slimefun.inventory.bypass") || player.isOp() || Slimefun.getProtectionManager().hasPermission(player, block, Interaction.INTERACT_BLOCK);
    }

    public boolean canOpen(@Nonnull final Block block, @Nonnull final Player player) {
        return canOpenDefaultImpl(block, player);
    }

    public abstract int[] getSlotsAccessedByItemTransport(final ItemTransportFlow itemTransportFlow);

    public BlockMenuPreset getMenuPreset() {
        return Slimefun.getRegistry().getMenuPresets().get(getId());
    }
}
