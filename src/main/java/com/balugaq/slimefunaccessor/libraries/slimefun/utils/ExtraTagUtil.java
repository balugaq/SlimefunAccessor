package com.balugaq.slimefunaccessor.libraries.slimefun.utils;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author balugaq
 */
public class ExtraTagUtil {
    private ExtraTagUtil() {
    }

    private static final Random RANDOM = new Random();
    public static final String BS_EXTRA_TAG_START = "__sf_accessor_extra_tag_";
    @ParametersAreNonnullByDefault
    public static void addExtraTag(final Location location, final String tag) {
        getData(location).ifPresent(data -> {
            data.setData(randomKey(), tag);
        });
    }

    @ParametersAreNonnullByDefault
    public static void removeExtraTag(final Location location, final String tag) {
        getData(location).ifPresent(data -> {
            Set<String> pendingRemoval = new HashSet<>();
            data.getAllData().forEach((key, value) -> {
                if (value.equals(tag)) {
                    pendingRemoval.add(key);
                }
            });

            for (String key : pendingRemoval) {
                data.removeData(key);
            }
        });
    }

    @ParametersAreNonnullByDefault
    public static void clearExtraTags(final Location location) {
        getData(location).ifPresent(data -> {
            Set<String> pendingRemoval = new HashSet<>();
            data.getAllData().forEach((key, value) -> {
                if (key.startsWith(BS_EXTRA_TAG_START)) {
                    pendingRemoval.add(key);
                }
            });

            for (String key : pendingRemoval) {
                data.removeData(key);
            }
        });
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static Optional<SlimefunBlockData> getData(final Location location) {
        SlimefunBlockData data = StorageCacheUtils.getBlock(location);
        return Optional.ofNullable(data);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static Set<String> getAllExtraTags(final Location location) {
        Set<String> tags = new HashSet<>();
        getData(location).ifPresent(data -> {
            data.getAllData().forEach((key, value) -> {
                if (key.startsWith(BS_EXTRA_TAG_START)) {
                    tags.add(value);
                }
            });
        });

        return tags;
    }

    @Nonnull
    public static String randomKey() {
        return BS_EXTRA_TAG_START + RANDOM.nextInt(Integer.MAX_VALUE);
    }
}
