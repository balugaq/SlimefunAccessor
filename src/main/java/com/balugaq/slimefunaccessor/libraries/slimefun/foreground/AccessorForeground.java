package com.balugaq.slimefunaccessor.libraries.slimefun.foreground;

import com.balugaq.slimefunaccessor.implementation.slimefun.Accessor;
import com.balugaq.slimefunaccessor.libraries.foreground.Foreground;
import com.balugaq.slimefunaccessor.libraries.slimefun.interfaces.BehaviorHandler;
import com.balugaq.slimefunaccessor.libraries.slimefun.utils.SlimefunItemUtil;
import com.balugaq.slimefunaccessor.libraries.utils.ChatUtils;
import com.balugaq.slimefunaccessor.libraries.utils.Pager;
import com.balugaq.slimefunaccessor.libraries.utils.ParticleUtil;
import com.balugaq.slimefunaccessor.libraries.utils.PdcUtil;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class AccessorForeground extends Foreground {
    public static final ItemStack AUTO_RELATED_ICON = new CustomItemStack(
            Material.COMPASS,
            "&6Auto-related",
            "&7Click to display machines related to the nearest item frame.",
            "&6Right-click to auto-related tags.",
            "&6Shift+Right-click to force override old tags and set new tags."
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

    public static List<Integer> displaySlots;
    static {
        final int[] rawDisplaySlots = MATRIX.getChars("D");
        for (final int rawDisplaySlot : rawDisplaySlots) {
            displaySlots.add(rawDisplaySlot);
        }
    }

    public AccessorForeground() {
        super();
    }

    public static void applyBlockMenuPreset(@Nonnull BlockMenuPreset preset) {
        MATRIX.build(preset);
    }

    public static void applyBlockMenu(@Nonnull BlockMenu menu) {
        menu.addMenuClickHandler(MATRIX.getChar("X"), (p, s, i, a) -> Behavior.AUTO_RELATED.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
        menu.addMenuClickHandler(MATRIX.getChar("D"), (p, s, i, a) -> Behavior.DISPLAY.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
        menu.addMenuClickHandler(MATRIX.getChar("S"), (p, s, i, a) -> Behavior.SEARCH.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
        menu.addMenuClickHandler(MATRIX.getChar("P"), (p, s, i, a) -> Behavior.PREV.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
        menu.addMenuClickHandler(MATRIX.getChar("N"), (p, s, i, a) -> Behavior.NEXT.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
    }

    public static class Behavior {
        public static final BehaviorHandler UPDATE_MENU = (pager, menu, u1, u2, u3, u4) -> {
            Location location = menu.getLocation();
            if (!menu.hasViewer()) {
                return false;
            }

            final Map<Location, SlimefunItem> slimefunItems = new HashMap<>();
            final List<Pager.Container<Location>> pages;
            final String filter = StorageCacheUtils.getData(location, Accessor.BS_FILTER_KEY);
            if (filter == null) {
                pages = pager.getPageLimited(displaySlots.size());
            } else {
                pages = pager.getPageLimited(1, displaySlots.size(), container -> {
                    Location loc = container.getData();
                    String tag = container.getTag();
                    final String itemName;
                    final SlimefunItem slimefunItem = StorageCacheUtils.getSfItem(loc);
                    if (slimefunItem != null) {
                        slimefunItems.put(loc, slimefunItem);
                        itemName = ChatColor.stripColor(slimefunItem.getItemName());
                    } else {
                        itemName = null;
                    }
                    return tag.toLowerCase().contains(filter.toLowerCase()) || itemName != null && itemName.toLowerCase().contains(filter.toLowerCase());
                });
            }

            for (int i = 0; i < displaySlots.size(); i++) {
                final int slot = displaySlots.get(i);
                if (i >= pages.size()) {
                    menu.replaceExistingItem(slot, ChestMenuUtils.getBackground());
                } else {
                    final Location loc = pages.get(i).getData();
                    final SlimefunItem slimefunItem = slimefunItems.get(loc);
                    if (slimefunItem != null) {
                        menu.replaceExistingItem(slot, SlimefunItemUtil.safeCopy(slimefunItem.getItem(), loc, slimefunItem.getId()));
                    }
                }
            }

            return false;
        };

        // Done
        public static final BehaviorHandler SEARCH = (pager, menu, p, u1, u2, u3) -> {
            Location location = menu.getLocation();
            p.closeInventory();
            ChatUtils.awaitInput(p, "Enter keywords to search: ", input -> {
                StorageCacheUtils.setData(location, Accessor.BS_FILTER_KEY, input);
                p.sendMessage("Set search keywords to: " + input);
                BlockMenu actualMenu = StorageCacheUtils.getMenu(location);
                if (actualMenu == null) {
                    return;
                }
                actualMenu.open(p);
                UPDATE_MENU.apply(pager, menu, p, u1, u2, u3);
            });
            return false;
        };

        public static final BehaviorHandler AUTO_RELATED = (pager, menu, u1, u2, u3, action) -> {
            boolean overrideOldTag = action.isShiftClicked();
            List<Pager.Container<Location>> pages = pager.getContent();
            for (Pager.Container<Location> container : pages) {
                if (!overrideOldTag && container.getTag() != null) {
                    continue;
                }

                Location location = container.getData();
                Collection<Entity> nearbyItemFrames = location.getWorld().getNearbyEntities(BoundingBox.of(location.getBlock()).expand(0.05), entity -> entity instanceof ItemFrame);
                Entity one = nearbyItemFrames.stream().findFirst().orElse(null);
                if (!(one instanceof ItemFrame itemFrame)) {
                    continue;
                }

                ItemStack itemStack = itemFrame.getItem();
                if (itemStack.getType() == Material.AIR) {
                    continue;
                }

                String tag = ItemStackHelper.getDisplayName(itemStack);
                container.setTag(tag);
            }
            return false;
        };

        // Done
        public static final BehaviorHandler PREV = (pager, menu, u2, u3, u4, u5) -> {
            pager.previous();
            UPDATE_MENU.apply(pager, menu, u2, u3, u4, u5);
            return false;
        };

        // Done
        public static final BehaviorHandler NEXT = (pager, menu, u2, u3, u4, u5) -> {
            pager.next();
            UPDATE_MENU.apply(pager, menu, u2, u3, u4, u5);
            return false;
        };

        // Done
        public static final BehaviorHandler ESP_BLOCK = (u1, u2, p, u3, item, u4) -> {
            PdcUtil.findLocationPdc(item).ifPresent(location -> {
                ParticleUtil.drawLineFrom(p.getLocation(), location);
                ParticleUtil.highlightBlock(location);
            });

            return false;
        };

        // Done
        public static final BehaviorHandler DISPLAY = (u1, u2, p, u3, item, action) -> {
            if (action.isRightClicked()) {
                ESP_BLOCK.apply(u1, u2, p, u3, item, action);
                return false;
            }

            PdcUtil.findLocationPdc(item).ifPresent(location -> {
                // open the menu of the machine
                BlockMenu actualMenu = StorageCacheUtils.getMenu(location);
                if (actualMenu == null) {
                    return;
                }
                actualMenu.open(p);
            });

            return false;
        };
    }

    public static void addAccessible(Location location) {
        World world = location.getWorld();
        for (Location root : getConnections().keySet()) {
            if (root.getWorld() == world && root.distance(location) < Accessor.RADIUS) {
                getConnections().get(root).add(location);
            }
        }
    }

    public static void removeAccessible(Location location) {
        World world = location.getWorld();
        for (Location root : getConnections().keySet()) {
            if (root.getWorld() == world && root.distance(location) < Accessor.RADIUS) {
                getConnections().get(root).remove(location);
            }
        }
    }
}
