package com.balugaq.slimefunaccessor.libraries.slimefun.foreground;

import com.balugaq.slimefunaccessor.implementation.listeners.InventoryListener;
import com.balugaq.slimefunaccessor.implementation.slimefun.Accessor;
import com.balugaq.slimefunaccessor.libraries.foreground.Foreground;
import com.balugaq.slimefunaccessor.libraries.slimefun.interfaces.BehaviorHandler;
import com.balugaq.slimefunaccessor.libraries.slimefun.utils.SlimefunItemUtil;
import com.balugaq.slimefunaccessor.libraries.utils.ChatUtils;
import com.balugaq.slimefunaccessor.libraries.utils.ItemStackUtil;
import com.balugaq.slimefunaccessor.libraries.utils.Pager;
import com.balugaq.slimefunaccessor.libraries.utils.ParticleUtil;
import com.balugaq.slimefunaccessor.libraries.utils.PdcUtil;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public static final String DEFAULT_COLOR = ChatColor.translateAlternateColorCodes('&', "&6");
    public static final ItemStack AUTO_RELATED_ICON = new CustomItemStack(
            Material.COMPASS,
            "&6自动标记",
            "&7点击以自动标记未标记的机器（寻找最近的展示框）",
            "&6Shift 点击以强制标记所有机器"
    );

    public static final ItemStack SEARCH_ICON = new CustomItemStack(
            Material.NAME_TAG,
            "&6搜索",
            "&7点击输入搜索关键词."
    );
    public static final ItemStack PREV_ICON = new CustomItemStack(
            Material.ARROW,
            "&6上一页",
            "&7点击翻到上一页."
    );
    public static final ItemStack NEXT_ICON = new CustomItemStack(
            Material.SPECTRAL_ARROW,
            "&6下一页",
            "&7点击翻到下一页."
    );

    public static final ItemStack RANGE_ICON = new CustomItemStack(
            Material.CLOCK,
            "&6范围",
            "&7点击设置远程范围.（1~200）"
    );

    public static final MenuMatrix MATRIX = new MenuMatrix()
            .addLine("DDDDDDDDX")
            .addLine("DDDDDDDDR")
            .addLine("DDDDDDDDB")
            .addLine("DDDDDDDDS")
            .addLine("DDDDDDDDP")
            .addLine("DDDDDDDDN")
            .addItem("X", AUTO_RELATED_ICON)
            .addItem("R", RANGE_ICON)
            .addItem("D", ChestMenuUtils.getBackground())
            .addItem("B", ChestMenuUtils.getBackground())
            .addItem("S", SEARCH_ICON)
            .addItem("P", PREV_ICON)
            .addItem("N", NEXT_ICON);

    public static List<Integer> displaySlots = new ArrayList<>();

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
        for (int slot : MATRIX.getChars("X")) {
            menu.addMenuClickHandler(slot, (p, s, i, a) -> Behavior.AUTO_RELATED.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
        }

        for (int slot : MATRIX.getChars("R")) {
            menu.addMenuClickHandler(slot, (p, s, i, a) -> Behavior.RANGE.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
        }

        for (int slot : MATRIX.getChars("D")) {
            menu.addMenuClickHandler(slot, (p, s, i, a) -> Behavior.DISPLAY.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
        }

        for (int slot : MATRIX.getChars("S")) {
            menu.addMenuClickHandler(slot, (p, s, i, a) -> Behavior.SEARCH.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
        }

        for (int slot : MATRIX.getChars("P")) {
            menu.addMenuClickHandler(slot, (p, s, i, a) -> Behavior.PREV.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
        }

        for (int slot : MATRIX.getChars("N")) {
            menu.addMenuClickHandler(slot, (p, s, i, a) -> Behavior.NEXT.apply(getConnection(menu.getLocation()), menu, p, s, i, a));
        }

        menu.addMenuOpeningHandler(player -> {
            Behavior.AUTO_RELATED.apply(
                    AccessorForeground.getConnection(menu.getLocation()),
                    menu,
                    null,
                    null,
                    null,
                    new ClickAction(false, false)
            );
            Accessor.AUTO_RELATED.add(menu.getLocation());
        });
    }

    public static void addAccessible(Location location) {
        World world = location.getWorld();
        for (Location root : getConnections().keySet()) {
            String rangeStr = StorageCacheUtils.getData(root, Accessor.BS_RANGE_KEY);
            int range = rangeStr == null ? Accessor.DEFAULT_RANGE : Integer.parseInt(rangeStr);
            if (root.getWorld() == world && root.distance(location) < range) {
                getConnections().get(root).add(location);
            }
        }
    }

    public static void removeAccessible(Location location) {
        World world = location.getWorld();
        for (Location root : getConnections().keySet()) {
            String rangeStr = StorageCacheUtils.getData(root, Accessor.BS_RANGE_KEY);
            int range = rangeStr == null ? Accessor.DEFAULT_RANGE : Integer.parseInt(rangeStr);
            if (root.getWorld() == world && root.distance(location) < range) {
                getConnections().get(root).remove(location);
            }
        }
    }

    public static class Behavior {
        // Done
        public static final BehaviorHandler UPDATE_MENU = (pager, menu, u1, u2, u3, u4) -> {
            final Location location = menu.getLocation();
            if (!menu.hasViewer()) {
                return false;
            }

            if (!pager.isDirty()) {
                return false;
            }

            AtomicBoolean dirty = new AtomicBoolean(false);
            final List<Pager.Container<Location>> pages;
            final String filter = StorageCacheUtils.getData(location, Accessor.BS_FILTER_KEY);
            if (filter == null) {
                pages = pager.getPageLimited(pager.getCurrentPage(), displaySlots.size());
            } else {
                pages = pager.getPageLimited(1, displaySlots.size(), container -> {
                    final String tag = container.getTag();
                    final String itemName;
                    final SlimefunItem slimefunItem = container.getSlimefunItem();
                    if (slimefunItem != null) {
                        itemName = ChatColor.stripColor(slimefunItem.getItemName());
                    } else {
                        final SlimefunItem item2 = StorageCacheUtils.getSfItem(container.getData());
                        if (item2 != null) {
                            container.setSlimefunItem(item2);
                            dirty.set(true);
                        }
                        itemName = null;
                    }
                    return tag != null && tag.toLowerCase().contains(filter.toLowerCase()) || itemName != null && itemName.toLowerCase().contains(filter.toLowerCase());
                });
            }

            for (int i = 0; i < displaySlots.size(); i++) {
                final int slot = displaySlots.get(i);
                if (i >= pages.size()) {
                    menu.replaceExistingItem(slot, ChestMenuUtils.getBackground());
                } else {
                    final Pager.Container<Location> container = pages.get(i);
                    final Location loc = container.getData();
                    final SlimefunItem slimefunItem = container.getSlimefunItem();
                    if (slimefunItem != null) {
                        menu.replaceExistingItem(slot, ItemStackUtil.resetDisplay(
                                SlimefunItemUtil.safeCopy(slimefunItem.getItem(), loc, slimefunItem.getId()),
                                container.getTag(),
                                slimefunItem.getItemName(),
                                ChatColor.translateAlternateColorCodes('&', "&7loc: " + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ())
                        ));
                    } else {
                        final SlimefunItem item2 = StorageCacheUtils.getSfItem(container.getData());
                        if (item2 != null) {
                            container.setSlimefunItem(item2);
                            dirty.set(true);
                        }
                    }
                }
            }

            pager.setDirty(dirty.get());

            return false;
        };

        // Done
        public static final BehaviorHandler SEARCH = (pager, menu, p, u1, u2, action) -> {
            Location location = menu.getLocation();
            if (action.isRightClicked()) {
                StorageCacheUtils.removeData(location, Accessor.BS_FILTER_KEY);
                return false;
            }

            p.closeInventory();
            ChatUtils.awaitInput(p, "输入检索关键词：", input -> {
                StorageCacheUtils.setData(location, Accessor.BS_FILTER_KEY, input);
                p.sendMessage("已设置关键词：" + input);
                BlockMenu actualMenu = StorageCacheUtils.getMenu(location);
                if (actualMenu == null) {
                    return;
                }
                actualMenu.open(p);
                UPDATE_MENU.apply(pager, menu, p, u1, u2, action);
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
                if (ChatColor.getLastColors(tag).isEmpty()) {
                    tag = DEFAULT_COLOR + tag;
                }
                container.setTag(tag);
            }
            pager.setDirty(true);
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
                ParticleUtil.drawLineFrom(p.getLocation(), location.clone().add(0.5, 0.5, 0.5));
                ParticleUtil.highlightBlock(location);
            });

            return false;
        };

        // Done
        public static final BehaviorHandler DISPLAY = (u1, menu, p, u3, item, action) -> {
            if (action.isRightClicked()) {
                ESP_BLOCK.apply(u1, menu, p, u3, item, action);
                return false;
            }

            PdcUtil.findLocationPdc(item).ifPresent(location -> {
                // open the menu of the machine
                BlockMenu actualMenu = StorageCacheUtils.getMenu(location);
                if (actualMenu == null) {
                    return;
                }
                actualMenu.open(p);
                InventoryListener.addRemoteAccessingPlayer(p, menu.getLocation());
            });

            return false;
        };

        public static final BehaviorHandler RANGE = (pager, menu, p, u1, u2, action) -> {
            Location location = menu.getLocation();
            if (action.isRightClicked()) {
                StorageCacheUtils.setData(location, Accessor.BS_RANGE_KEY, String.valueOf(Accessor.DEFAULT_RANGE));
                pager.clear();
                Accessor.load(location, pager, Accessor.DEFAULT_RANGE);
                return false;
            }

            p.closeInventory();
            ChatUtils.awaitInput(p, "输入远程范围：（1~200）", input -> {
                try {
                    int range = Integer.parseInt(input);
                    if (range < 1 || range > 200) {
                        p.sendMessage("输入范围必须在1~200之间！");
                        return;
                    }
                    StorageCacheUtils.setData(location, Accessor.BS_RANGE_KEY, String.valueOf(range));
                    Accessor.load(location, newPager(), range);
                } catch (NumberFormatException e) {
                    p.sendMessage("输入范围必须是整数！");
                    return;
                }

                p.sendMessage("已设置远程范围为： " + input);
                BlockMenu actualMenu = StorageCacheUtils.getMenu(location);
                if (actualMenu == null) {
                    return;
                }
                actualMenu.open(p);
                UPDATE_MENU.apply(pager, menu, p, u1, u2, action);
            });
            return false;
        };
    }
}
