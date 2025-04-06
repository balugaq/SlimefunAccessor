package com.balugaq.slimefunaccessor.libraries.slimefun.foreground;

import com.balugaq.slimefunaccessor.implementation.listeners.InventoryListener;
import com.balugaq.slimefunaccessor.implementation.main.SlimefunAccessorPlugin;
import com.balugaq.slimefunaccessor.implementation.slimefun.Accessor;
import com.balugaq.slimefunaccessor.libraries.foreground.Foreground;
import com.balugaq.slimefunaccessor.libraries.managers.IntegrationManager;
import com.balugaq.slimefunaccessor.libraries.slimefun.interfaces.BehaviorHandler;
import com.balugaq.slimefunaccessor.libraries.slimefun.utils.ExtraTagUtil;
import com.balugaq.slimefunaccessor.libraries.slimefun.utils.SlimefunUtil;
import com.balugaq.slimefunaccessor.libraries.utils.ChatUtil;
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
import org.bukkit.Bukkit;
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Menu should be applied like this:
 * <p>
 * X = Auto related to the nearest {@link ItemFrame}
 * R = Range button, click to set the range of remote machines.
 * H = Help button, hover to see the help message.
 * D = Displayed Machines.
 * B = {@link ChestMenuUtils#getBackground()}.
 * S = Searcher button, click to input keywords to search.
 * P = Previous page button.
 * N = Next page button.
 * <p>
 * DDDDDDDDX
 * DDDDDDDDR
 * DDDDDDDDH
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

    public static final ItemStack HELP_ICON = new CustomItemStack(
            Material.BOOK,
            "&6帮助",
            "&7左键机器: 打开机器菜单",
            "&7右键机器: 高亮方块",
            "&7Shift + 左键机器: 给机器添加额外标签",
            "&7Shift + 右键机器: 移除所有额外标签"
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
            .addLine("DDDDDDDDH")
            .addLine("DDDDDDDDS")
            .addLine("DDDDDDDDP")
            .addLine("DDDDDDDDN")
            .addItem("X", AUTO_RELATED_ICON)
            .addItem("R", RANGE_ICON)
            .addItem("H", HELP_ICON)
            .addItem("D", ChestMenuUtils.getBackground())
            .addItem("B", ChestMenuUtils.getBackground())
            .addItem("S", SEARCH_ICON)
            .addItem("P", PREV_ICON)
            .addItem("N", NEXT_ICON);

    public static final List<Integer> displaySlots = new ArrayList<>();

    static {
        final int[] rawDisplaySlots = MATRIX.getChars("D");
        for (final int rawDisplaySlot : rawDisplaySlots) {
            displaySlots.add(rawDisplaySlot);
        }
    }

    public AccessorForeground() {
        super();
    }

    public static void applyBlockMenuPreset(@Nonnull final BlockMenuPreset preset) {
        MATRIX.build(preset);
    }

    public static void applyBlockMenu(@Nonnull final BlockMenu menu) {
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

    public static void addAccessible(final Location location) {
        final World world = location.getWorld();
        for (final Location root : getConnections().keySet()) {
            final String rangeStr = StorageCacheUtils.getData(root, Accessor.BS_RANGE_KEY);
            final int range = rangeStr == null ? Accessor.DEFAULT_RANGE : Integer.parseInt(rangeStr);
            if (root.getWorld() == world && root.distance(location) < range) {
                getConnections().get(root).add(location);
            }
        }
    }

    public static void removeAccessible(final Location location) {
        final World world = location.getWorld();
        for (final Location root : getConnections().keySet()) {
            final String rangeStr = StorageCacheUtils.getData(root, Accessor.BS_RANGE_KEY);
            final int range = rangeStr == null ? Accessor.DEFAULT_RANGE : Integer.parseInt(rangeStr);
            if (root.getWorld() == world && root.distance(location) < range) {
                getConnections().get(root).remove(location);
            }
        }
    }

    public static class Behavior {
        // Done
        public static final BehaviorHandler UPDATE_MENU = (pager, menu, player, u2, u3, u4) -> {
            final Location location = menu.getLocation();
            if (!menu.hasViewer()) {
                return false;
            }

            if (!pager.isDirty()) {
                return false;
            }

            final AtomicBoolean dirty = new AtomicBoolean(false);
            final List<Pager.Container<Location>> pages;
            final String filter = StorageCacheUtils.getData(location, Accessor.BS_FILTER_KEY);
            if (filter == null) {
                pages = pager.getPageLimited(pager.getCurrentPage(), displaySlots.size());
            } else {
                pages = pager.getPageLimited(1, displaySlots.size(), container -> {
                    final Set<String> tags = container.getTags();
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
                    final boolean preMatch = tags != null && tags.stream().anyMatch(tag -> tag.contains(filter.toLowerCase())) || itemName != null && itemName.toLowerCase().contains(filter.toLowerCase());
                    if (preMatch) {
                        return true;
                    }

                    if (slimefunItem != null && IntegrationManager.isJEGLoaded()) {
                        return IntegrationManager.isSearchApplicable(player, slimefunItem, filter);
                    }

                    return false;
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
                        menu.replaceExistingItem(slot, getDisplayItem(container, slimefunItem, loc));
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
            final Location location = menu.getLocation();
            if (action.isRightClicked()) {
                StorageCacheUtils.removeData(location, Accessor.BS_FILTER_KEY);
                pager.setDirty(true);
                UPDATE_MENU.apply(pager, menu, p, u1, u2, action);
                return false;
            }

            p.closeInventory();
            ChatUtil.awaitInput(p, "输入检索关键词：", input -> {
                StorageCacheUtils.setData(location, Accessor.BS_FILTER_KEY, input);
                p.sendMessage("已设置关键词：" + input);
                final BlockMenu actualMenu = StorageCacheUtils.getMenu(location);
                if (actualMenu == null) {
                    return;
                }
                pager.setDirty(true);
                UPDATE_MENU.apply(pager, menu, p, u1, u2, action);
                actualMenu.open(p);
            });
            return false;
        };

        public static final BehaviorHandler AUTO_RELATED = (pager, menu, u1, u2, u3, action) -> {
            final boolean overrideOldTag = action.isShiftClicked();
            final List<Pager.Container<Location>> pages = pager.getContent();
            for (final Pager.Container<Location> container : pages) {
                container.clearTags();
                if (!overrideOldTag && !container.getTag().equals(Pager.Container.NOTHING)) {
                    continue;
                }

                final Location location = container.getData();
                final Collection<Entity> nearbyItemFrames = location.getWorld().getNearbyEntities(BoundingBox.of(location.getBlock()).expand(0.05), entity -> entity instanceof ItemFrame);
                for (final Entity one : nearbyItemFrames) {
                    if (!(one instanceof ItemFrame itemFrame)) {
                        continue;
                    }

                    final ItemStack itemStack = itemFrame.getItem();
                    if (itemStack.getType() == Material.AIR) {
                        continue;
                    }

                    String tag = ItemStackHelper.getDisplayName(itemStack);
                    if (ChatColor.getLastColors(tag).isEmpty()) {
                        tag = DEFAULT_COLOR + tag;
                    }
                    container.addTag(tag);
                    container.setItemOnFrame(itemStack);
                }

                final Set<String> extraTags = ExtraTagUtil.getAllExtraTags(location);
                for (final String tag : extraTags) {
                    container.addTag(tag);
                }
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
                for (int i = 0 ; i < 5; i++) {
                    final long delay = i * 30L;
                    Bukkit.getScheduler().runTaskLater(SlimefunAccessorPlugin.instance(), () -> {
                        ParticleUtil.drawLineFrom(p.getLocation(), location.clone().add(0.5, 0.5, 0.5));
                        ParticleUtil.highlightBlock(location);
                    }, delay);
                }
            });

            return false;
        };

        public static final BehaviorHandler OPEN_MENU = (u1, menu, p, u3, item, action) -> {
            PdcUtil.findLocationPdc(item).ifPresent(location -> {
                // open the menu of the machine
                final BlockMenu actualMenu = StorageCacheUtils.getMenu(location);
                if (actualMenu == null) {
                    return;
                }
                actualMenu.open(p);
                InventoryListener.addRemoteAccessingPlayer(p, menu.getLocation());
            });

            return false;
        };

        public static final BehaviorHandler ADD_EXTRA_TAG = (pager, u2, p, u3, item, u4) -> {
            PdcUtil.findLocationPdc(item).ifPresent(location -> {
                p.closeInventory();
                ChatUtil.awaitInput(p, "输入自定义标签：", input -> {
                    ExtraTagUtil.addExtraTag(location, input);
                    p.sendMessage("已添加自定义标签：" + input);
                    final BlockMenu actualMenu = StorageCacheUtils.getMenu(location);
                    if (actualMenu == null) {
                        return;
                    }
                    actualMenu.open(p);
                    pager.setDirty(true);
                });
            });

            return false;
        };

        public static final BehaviorHandler CLEAR_EXTRA_TAG = (pager, u2, p, u3, item, u4) -> {
            PdcUtil.findLocationPdc(item).ifPresent(location -> {
                ExtraTagUtil.clearExtraTags(location);
                pager.setDirty(true);
            });

            return false;
        };

        // Done
        public static final BehaviorHandler DISPLAY = (pager, menu, p, u3, item, action) -> {
            // Shift-right-click
            if (action.isShiftClicked() && action.isRightClicked()) {
                // Clear custom tag of the machine
                CLEAR_EXTRA_TAG.apply(pager, menu, p, u3, item, action);
                return false;
            }

            // Right-click
            if (action.isRightClicked()) {
                // ESP the block
                ESP_BLOCK.apply(pager, menu, p, u3, item, action);
                return false;
            }

            // Shift-left-click
            if (action.isShiftClicked()) {
                // Add custom tag to the machine
                ADD_EXTRA_TAG.apply(pager, menu, p, u3, item, action);
                return false;
            }

            // Left-click
            OPEN_MENU.apply(pager, menu, p, u3, item, action);
            return false;
        };

        public static final BehaviorHandler RANGE = (pager, menu, p, u1, u2, action) -> {
            final Location location = menu.getLocation();
            if (action.isRightClicked()) {
                StorageCacheUtils.setData(location, Accessor.BS_RANGE_KEY, String.valueOf(Accessor.DEFAULT_RANGE));
                pager.clear();
                Accessor.load(location, pager, Accessor.DEFAULT_RANGE);
                return false;
            }

            p.closeInventory();
            ChatUtil.awaitInput(p, "输入远程范围：（1~200）", input -> {
                try {
                    final int range = Integer.parseInt(input);
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
                final BlockMenu actualMenu = StorageCacheUtils.getMenu(location);
                if (actualMenu == null) {
                    return;
                }
                actualMenu.open(p);
                UPDATE_MENU.apply(pager, menu, p, u1, u2, action);
            });
            return false;
        };
    }

    public static ItemStack getDisplayItem(final Pager.Container<Location> container, final SlimefunItem slimefunItem, final Location loc) {
        final ItemStack itemOnFrame = container.getItemOnFrame();
        return ItemStackUtil.resetDisplay(
                SlimefunUtil.safeCopy(itemOnFrame == null ? slimefunItem.getItem() : itemOnFrame, loc, slimefunItem.getId()),
                container.getTag(),
                slimefunItem.getItemName(),
                ChatColor.translateAlternateColorCodes('&', "&7loc: " + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ()),
                container.getTags().toArray(new String[0])
        );
    }
}
