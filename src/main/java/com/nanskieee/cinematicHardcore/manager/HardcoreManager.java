package com.nanskieee.cinematicHardcore.manager;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import com.nanskieee.cinematicHardcore.config.ConfigManager;
import com.nanskieee.cinematicHardcore.data.PlayerData;
import com.nanskieee.cinematicHardcore.utils.TextFormatter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class HardcoreManager {
    private final CinematicHardcore plugin;
    private final PlayerDataManager dataManager;
    private final ConfigManager config;

    public HardcoreManager(CinematicHardcore plugin, PlayerDataManager dataManager, ConfigManager config) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.config = config;
    }

    public boolean isEnabled() {
        return config.getBoolean("hardcore.enabled", true);
    }

    public boolean isDead(Player player) {
        return dataManager.get(player.getUniqueId()).isHardcoreDead();
    }

    public void markDead(Player player) {
        PlayerData data = dataManager.get(player.getUniqueId());
        data.setHardcoreDead(true);
        dataManager.save(player.getUniqueId());
        plugin.getLogger().info("[Hardcore] " + player.getName() + " telah mati permanen.");
    }

    public void applyOnJoin(Player player) {
        if (!isEnabled() || player.hasPermission("cinematichardcore.bypass")) return;

        if (isDead(player)) {
            player.setGameMode(GameMode.SPECTATOR);
            String msg = config.getString("hardcore.death-message", "<red>Kamu telah mati dalam mode Hardcore. Kamu berada di Spectator.</red>");
            plugin.adventure(player).sendMessage(TextFormatter.parseForPlayer(msg, player));
        }
    }
}