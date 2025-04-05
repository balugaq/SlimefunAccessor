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

public class ExtraTagUtils {
    private static final Random random = new Random();
    public static final String BS_EXTRA_TAG_START = "__sf_accessor_extra_tag_";
    @ParametersAreNonnullByDefault
    public static void addExtraTag(Location location, String tag) {
        getData(location).ifPresent(data -> {
            data.setData(randomKey(), tag);
        });
    }

    @ParametersAreNonnullByDefault
    public static void removeExtraTag(Location location, String tag) {
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
    public static void clearExtraTags(Location location) {
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
    public static Optional<SlimefunBlockData> getData(Location location) {
        SlimefunBlockData data = StorageCacheUtils.getBlock(location);
        return Optional.ofNullable(data);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public static Set<String> getAllExtraTags(Location location) {
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
        return BS_EXTRA_TAG_START + random.nextInt(Integer.MAX_VALUE);
    }
}
