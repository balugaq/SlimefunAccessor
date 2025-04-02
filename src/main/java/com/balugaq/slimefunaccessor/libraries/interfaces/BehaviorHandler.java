package com.balugaq.slimefunaccessor.libraries.interfaces;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BehaviorHandler extends EFunction<BlockMenu, Player, Integer, ItemStack, ClickAction, Boolean> {
}
