package com.nanskieee.cinematicHardcore.listener;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import com.nanskieee.cinematicHardcore.data.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final CinematicHardcore plugin;

    public PlayerJoinListener(CinematicHardcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());
        data.setPlayerName(player.getName());

        plugin.getHardcoreManager().applyOnJoin(player);

        // Cek Anti-Crash (Jika sebelumnya terputus di tengah cinematic)
        if (data.isInCinematic()) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline()) plugin.getCinematicManager().start(player); // Ini akan memicu fungsi restore
            }, 10L);
            return;
        }

        boolean firstJoinOnly = plugin.getConfigManager().getBoolean("settings.first-join-only", true);
        boolean bypass = player.hasPermission("cinematichardcore.bypass");

        if (!data.hasCompletedIntro() || !firstJoinOnly) {
            if (!bypass) {
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    if (player.isOnline()) plugin.getCinematicManager().start(player);
                }, 20L);
            }
        }
    }
}