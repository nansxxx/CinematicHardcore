package com.nanskieee.cinematicHardcore.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class SoundUtils {
    private SoundUtils() {}

    public static void play(Player player, String soundName, float volume, float pitch) {
        if (soundName == null || soundName.isEmpty()) return;
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException ignored) {}
    }
}