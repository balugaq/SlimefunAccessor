package com.balugaq.slimefunaccessor.libraries.interfaces;

import com.balugaq.slimefunaccessor.libraries.utils.Pager;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BehaviorHandler extends FFunction<Pager<Location>, BlockMenu, Player, Integer, ItemStack, ClickAction, Boolean> {
}
