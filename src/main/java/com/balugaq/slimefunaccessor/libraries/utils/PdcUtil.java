package com.balugaq.slimefunaccessor.libraries.utils;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 *
 * @author balugaq
 */
public class PdcUtil {
    private PdcUtil() {
    }

    public static final NamespacedKey LOCATION_KEY = KeyUtils.newKey("location");
    public static final NamespacedKey MIRROR_SLIMEFUN_ID_KEY = KeyUtils.newKey("mirror_slimefun_id");

    @Nonnull
    public static Optional<Location> findLocationPdc(@Nonnull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }

        String locationStr = meta.getPersistentDataContainer().get(LOCATION_KEY, PersistentDataType.STRING);
        if (locationStr == null) {
            return Optional.empty();
        }

        Location location = StringUtil.string2Location(locationStr);
        return Optional.ofNullable(location);
    }

    public static void setLocationPdc(@Nonnull final ItemStack itemStack, @Nonnull final Location location) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        // str: world;x:y:z
        String locationStr = StringUtil.location2String(location);
        meta.getPersistentDataContainer().set(LOCATION_KEY, PersistentDataType.STRING, locationStr);
        itemStack.setItemMeta(meta);
    }

    @Nonnull
    public static Optional<String> findMirrorSlimefunIdPdc(@Nonnull final ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return Optional.empty();
        }

        String mirrorSlimefunId = meta.getPersistentDataContainer().get(MIRROR_SLIMEFUN_ID_KEY, PersistentDataType.STRING);
        if (mirrorSlimefunId == null) {
            return Optional.empty();
        }

        return Optional.of(mirrorSlimefunId);
    }

    public static void setMirrorSlimefunIdPdc(@Nonnull final ItemStack itemStack, @Nonnull final String mirrorSlimefunId) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        meta.getPersistentDataContainer().set(MIRROR_SLIMEFUN_ID_KEY, PersistentDataType.STRING, mirrorSlimefunId);
        itemStack.setItemMeta(meta);
    }
}
