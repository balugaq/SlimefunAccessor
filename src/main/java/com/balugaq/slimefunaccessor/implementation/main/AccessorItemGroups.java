package com.balugaq.slimefunaccessor.implementation.main;

import com.balugaq.slimefunaccessor.libraries.utils.KeyUtils;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
/**
 *
 * @author balugaq
 */
public class AccessorItemGroups {
    private AccessorItemGroups() {
    }

    public static NestedItemGroup MAIN;
    public static SubItemGroup MACHINES;

    public static void setup() {
        MAIN = new NestedItemGroup(
                KeyUtils.newKey("main"),
                AccessorIcons.MAIN
        );

        MACHINES = new SubItemGroup(
                KeyUtils.newKey("machines"),
                MAIN,
                AccessorIcons.MACHINES
        );
    }
}
