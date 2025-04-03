package com.balugaq.slimefunaccessor.libraries.slimefun.utils;

import com.balugaq.slimefunaccessor.libraries.utils.ItemStackUtil;
import com.balugaq.slimefunaccessor.libraries.utils.PdcUtil;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class SlimefunItemUtil {
    public static ItemStack safeCopy(ItemStack itemStack, Location location, String id) {
        ItemStack copy = ItemStackUtil.safeCopy(itemStack);
        PdcUtil.setLocationPdc(copy, location);
        PdcUtil.setMirrorSlimefunIdPdc(copy, id);
        return copy;
    }
}
