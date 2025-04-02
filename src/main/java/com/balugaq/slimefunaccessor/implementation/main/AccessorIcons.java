package com.balugaq.slimefunaccessor.implementation.main;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AccessorIcons {
    public static final SlimefunItemStack ACCESSOR = new SlimefunItemStack(
            "ACCESSOR_ACCESSOR",
            Material.OBSERVER,
            "&bSlimefun Accessor",
            "&7This item is used to access Slimefun machines."
    );

    public static final ItemStack MAIN = new CustomItemStack(
            Material.OBSERVER,
            "&bSlimefun Accessor"
    );

    public static final ItemStack MACHINES = new CustomItemStack(
            Material.CARTOGRAPHY_TABLE,
            "&6Machines"
    );
}
