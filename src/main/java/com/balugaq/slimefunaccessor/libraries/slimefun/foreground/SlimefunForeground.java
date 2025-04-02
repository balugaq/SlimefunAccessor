package com.balugaq.slimefunaccessor.libraries.slimefun.foreground;

import com.balugaq.slimefunaccessor.libraries.foreground.Foreground;
import com.balugaq.slimefunaccessor.libraries.foreground.MenuMatrix;
import com.balugaq.slimefunaccessor.libraries.interfaces.BehaviorHandler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Menu should be applied like this:
 * <p>
 * X = Auto related to the nearest {@link ItemFrame}
 * D = Displayed Machines.
 * B = {@link ChestMenuUtils#getBackground()}.
 * S = Searcher button, click to input keywords to search.
 * P = Previous page button.
 * N = Next page button.
 * <p>
 * DDDDDDDDX
 * DDDDDDDDB
 * DDDDDDDDB
 * DDDDDDDDS
 * DDDDDDDDP
 * DDDDDDDDN
 *
 * @author baluagq
 */
public class SlimefunForeground extends Foreground {
    public static final ItemStack AUTO_RELATED_ICON = new CustomItemStack(
            Material.COMPASS,
            "&6Auto-related",
            "&7Click to display machines related to the nearest item frame."
    );

    public static final ItemStack SEARCH_ICON = new CustomItemStack(
            Material.NAME_TAG,
            "&6Search",
            "&7Click to input keywords to search."
    );
    public static final ItemStack PREV_ICON = new CustomItemStack(
            Material.ARROW,
            "&6Previous",
            "&7Click to go to previous page."
    );
    public static final ItemStack NEXT_ICON = new CustomItemStack(
            Material.SPECTRAL_ARROW,
            "&6Next",
            "&7Click to go to next page."
    );
    public static final MenuMatrix MATRIX = new MenuMatrix()
            .addLine("DDDDDDDDX")
            .addLine("DDDDDDDDB")
            .addLine("DDDDDDDDB")
            .addLine("DDDDDDDDS")
            .addLine("DDDDDDDDP")
            .addLine("DDDDDDDDN")
            .addItem("X", AUTO_RELATED_ICON)
            .addItem("D", ChestMenuUtils.getBackground())
            .addItem("B", ChestMenuUtils.getBackground())
            .addItem("S", SEARCH_ICON)
            .addItem("P", PREV_ICON)
            .addItem("N", NEXT_ICON);

    public SlimefunForeground() {
        super();
        connection.setContentPerPage(6 * 8);
    }

    public static void applyBlockMenuPreset(@Nonnull BlockMenuPreset menu) {
        MATRIX.build(menu);
    }

    public static void applyBlockMenu(@Nonnull BlockMenu menu) {
        menu.addMenuClickHandler(MATRIX.getChar("S"), (p, s, i, a) -> Behavior.SEARCH.apply(menu, p, s, i, a));
    }

    public static class Behavior {
        public static final BehaviorHandler SEARCH = (menu, p, slot, item, action) -> {
            Location location = menu.getLocation();

            return false;
        };

        public static final BehaviorHandler AUTO_RELATED = (menu, p, slot, item, action) -> {
            Location location = menu.getLocation();

            return false;
        };

        public static final BehaviorHandler DISPLAY = (menu, p, slot, item, action) -> {
            Location location = menu.getLocation();

            return false;
        };

        public static final BehaviorHandler PREV = (menu, p, slot, item, action) -> {
            Location location = menu.getLocation();

            return false;
        };

        public static final BehaviorHandler NEXT = (menu, p, slot, item, action) -> {
            Location location = menu.getLocation();

            return false;
        };

        public static final BehaviorHandler ESP_BLOCK = (menu, p, slot, item, action) -> {
            Location location = menu.getLocation();

            return false;
        };
    }
}
