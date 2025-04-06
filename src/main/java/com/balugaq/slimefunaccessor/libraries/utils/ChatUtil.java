package com.balugaq.slimefunaccessor.libraries.utils;

import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 *
 * @author balugaq
 */
public class ChatUtil {
    private ChatUtil() {
    }

    public static void awaitInput(@Nonnull final Player player, @Nonnull final String message, @Nonnull final Consumer<String> callback) {
        player.sendMessage(message);
        ChatUtils.awaitInput(player, callback);
    }
}
