package com.nanskieee.cinematicHardcore.utils;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public final class TextFormatter {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private TextFormatter() {}

    public static Component parse(String input) {
        if (input == null) return Component.empty();
        return MM.deserialize(input);
    }

    public static Component parseForPlayer(String input, Player player) {
        if (input == null) return Component.empty();
        String replaced = input.replace("{player}", player.getName());
        return MM.deserialize(replaced);
    }

    // Fungsi khusus untuk PAPI (Placeholder API)
    public static Component parseWithPapi(CinematicHardcore plugin, String input, Player player) {
        if (input == null) return Component.empty();
        String replaced = input.replace("{player}", player.getName());

        if (plugin.hasPapi()) {
            try {
                replaced = PlaceholderAPI.setPlaceholders(player, replaced);
            } catch (NoClassDefFoundError ignored) {}
        }

        return MM.deserialize(replaced);
    }
}