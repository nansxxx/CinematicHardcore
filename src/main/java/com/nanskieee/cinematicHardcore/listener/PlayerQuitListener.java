package com.nanskieee.cinematicHardcore.listener;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final CinematicHardcore plugin;

    public PlayerQuitListener(CinematicHardcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // Simpan data pemain
        plugin.getPlayerDataManager().save(event.getPlayer().getUniqueId());

        // Hentikan cinematic jika sedang berjalan
        if (plugin.getCinematicManager().isInCinematic(event.getPlayer())) {
            plugin.getCinematicManager().skip(event.getPlayer());
        }
    }
}