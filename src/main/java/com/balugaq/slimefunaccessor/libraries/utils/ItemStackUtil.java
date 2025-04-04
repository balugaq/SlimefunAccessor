package com.balugaq.slimefunaccessor.libraries.utils;

import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedItemFlag;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;

public class ItemStackUtil {
    public static ItemStack safeCopy(ItemStack legacy) {
        Material material = legacy.getType();
        ItemStack itemStack;
        if (material == Material.PLAYER_HEAD || material == Material.PLAYER_WALL_HEAD) {
            String hash = getHash(legacy);
            if (hash != null) {
                itemStack = PlayerHead.getItemStack(PlayerSkin.fromHashCode(hash));
            } else {
                itemStack = new ItemStack(material);
            }
        } else {
            itemStack = new ItemStack(material);
        }
        itemStack.setAmount(legacy.getAmount());

        ItemMeta legacyMeta = legacy.getItemMeta();
        ItemMeta meta = itemStack.getItemMeta();

        meta.addItemFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                VersionedItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        if (legacyMeta.hasDisplayName()) {
            String name = legacyMeta.getDisplayName();
            meta.setDisplayName(" " + name + " ");
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static String getHash(ItemStack item) {
        if (item != null && (item.getType() == Material.PLAYER_HEAD || item.getType() == Material.PLAYER_WALL_HEAD)) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof SkullMeta) {
                try {
                    URL t = ((SkullMeta) meta).getOwnerProfile().getTextures().getSkin();
                    String path = t.getPath();
                    String[] parts = path.split("/");
                    return parts[parts.length - 1];
                } catch (Throwable ignored) {
                }
            }
        }
        return null;
    }

    public static ItemStack resetDisplay(@Nullable ItemStack original, @Nullable String name) {
        return resetDisplay(original, name, (String[]) null);
    }

    public static ItemStack resetDisplay(@Nullable ItemStack original, @Nullable String name, @Nullable String... lore) {
        if (original == null) {
            return null;
        }

        if (name == null) {
            return original;
        }

        ItemStack itemStack = original.clone();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return itemStack;
        }

        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
