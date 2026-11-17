package com.nanskieee.cinematicHardcore.listener;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private final CinematicHardcore plugin;

    public PlayerDeathListener(CinematicHardcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        if (!plugin.getHardcoreManager().isEnabled()) return;
        if (player.hasPermission("cinematichardcore.bypass")) return;

        // Tandai pemain mati permanen
        plugin.getHardcoreManager().markDead(player);
    }
}