package com.balugaq.slimefunaccessor.libraries.utils;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ChatUtils {
    public static void awaitInput(Player player, String message, Consumer<String> callback) {
        player.sendMessage(message);
        io.github.thebusybiscuit.slimefun4.utils.ChatUtils.awaitInput(player, callback);
    }
}
