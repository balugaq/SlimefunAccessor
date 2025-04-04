package com.balugaq.slimefunaccessor.libraries.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StringUtil {
    @Nonnull
    public static String location2String(@Nonnull Location location) {
        return location.getWorld().getName() + ";" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }

    @Nullable
    public static Location string2Location(@Nonnull String string) {
        // str: world;x:y:z
        String[] split = string.split(";");
        if (split.length != 2) {
            return null;
        }

        String[] xyz = split[1].split(":");
        if (xyz.length != 3) {
            return null;
        }

        try {
            int x = Integer.parseInt(xyz[0]);
            int y = Integer.parseInt(xyz[1]);
            int z = Integer.parseInt(xyz[2]);
            return new Location(Bukkit.getWorld(split[0]), x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
