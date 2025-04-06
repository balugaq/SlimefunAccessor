package com.balugaq.slimefunaccessor.libraries.utils;

import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedItemFlag;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author balugaq
 */
public class ItemStackUtil {
    private ItemStackUtil() {
    }

    @Nonnull
    public static ItemStack safeCopy(@Nonnull final ItemStack legacy) {
        final Material material = legacy.getType();
        final ItemStack itemStack;
        if (material == Material.PLAYER_HEAD || material == Material.PLAYER_WALL_HEAD) {
            final String hash = getHash(legacy);
            if (hash != null) {
                itemStack = PlayerHead.getItemStack(PlayerSkin.fromHashCode(hash));
            } else {
                itemStack = new ItemStack(material);
            }
        } else {
            itemStack = new ItemStack(material);
        }
        itemStack.setAmount(legacy.getAmount());

        final ItemMeta legacyMeta = legacy.getItemMeta();
        final ItemMeta meta = itemStack.getItemMeta();

        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                VersionedItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        if (legacyMeta.hasDisplayName()) {
            final String name = legacyMeta.getDisplayName();
            meta.setDisplayName(" " + name + " ");
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Nullable
    public static String getHash(@Nullable final ItemStack item) {
        if (item != null && (item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD)) {
            final ItemMeta meta = item.getItemMeta();
            if (meta instanceof SkullMeta) {
                try {
                    final URL t = ((SkullMeta) meta).getOwnerProfile().getTextures().getSkin();
                    final String path = t.getPath();
                    final String[] parts = path.split("/");
                    return parts[parts.length - 1];
                } catch (Throwable ignored) {
                }
            }
        }
        return null;
    }

    public static ItemStack resetDisplay(@Nullable final ItemStack original, @Nullable final String name) {
        return resetDisplay(original, name, (String[]) null);
    }

    public static ItemStack resetDisplay(@Nullable final ItemStack original, @Nullable final String name, @Nullable final String lore, @Nullable final String lore2, @Nullable final String... lore3) {
        // merge lore, lore2, lore3 into lores
        final List<String> lores = new ArrayList<>();
        if (lore != null) {
            lores.add(lore);
        }

        if (lore2 != null) {
            lores.add(lore2);
        }

        if (lore3 != null) {
            lores.addAll(List.of(lore3));
        }

        return resetDisplay(original, name, lores.toArray(new String[0]));
    }

    public static ItemStack resetDisplay(@Nullable final ItemStack original, @Nullable final String name, @Nullable final String... lore) {
        if (original == null) {
            return null;
        }

        final ItemStack itemStack = original.clone();
        final ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return itemStack;
        }

        if (name != null) {
            meta.setDisplayName(name);
        }
        if (lore != null && lore.length > 0) {
            meta.setLore(List.of(lore));
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
