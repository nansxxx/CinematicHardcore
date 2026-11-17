package com.nanskieee.cinematicHardcore;

import com.nanskieee.cinematicHardcore.command.ChangeLangCommand;
import com.nanskieee.cinematicHardcore.command.GuideCommand;
import com.nanskieee.cinematicHardcore.command.IntroCommand;
import com.nanskieee.cinematicHardcore.config.ConfigManager;
import com.nanskieee.cinematicHardcore.gui.LanguageGUI;
import com.nanskieee.cinematicHardcore.listener.PlayerDeathListener;
import com.nanskieee.cinematicHardcore.listener.PlayerJoinListener;
import com.nanskieee.cinematicHardcore.listener.PlayerQuitListener;
import com.nanskieee.cinematicHardcore.manager.*;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CinematicHardcore extends JavaPlugin {

    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private DeploymentManager deploymentManager;
    private HardcoreManager hardcoreManager;
    private CinematicManager cinematicManager;
    private LanguageManager languageManager;

    private boolean hasPapi = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("lang/id.yml", false);
        saveResource("lang/en.yml", false);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) hasPapi = true;

        this.configManager = new ConfigManager(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.languageManager = new LanguageManager(this);
        this.deploymentManager = new DeploymentManager(this, configManager);
        this.hardcoreManager = new HardcoreManager(this, playerDataManager, configManager);
        this.cinematicManager = new CinematicManager(this, configManager, deploymentManager, playerDataManager);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new LanguageGUI(this), this);

        this.getCommand("guide").setExecutor(new GuideCommand(this));
        this.getCommand("intro").setExecutor(new IntroCommand(this));
        this.getCommand("intro").setTabCompleter(new IntroCommand(this));
        this.getCommand("changelang").setExecutor(new ChangeLangCommand(this));

        getLogger().info("CinematicHardcore berhasil diaktifkan dengan fitur Multi-Language!");
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) {
            for (Player p : getServer().getOnlinePlayers()) {
                playerDataManager.save(p.getUniqueId());
            }
        }
    }

    public ConfigManager getConfigManager() { return configManager; }
    public PlayerDataManager getPlayerDataManager() { return playerDataManager; }
    public DeploymentManager getDeploymentManager() { return deploymentManager; }
    public HardcoreManager getHardcoreManager() { return hardcoreManager; }
    public CinematicManager getCinematicManager() { return cinematicManager; }
    public LanguageManager getLanguageManager() { return languageManager; }
    public boolean hasPapi() { return hasPapi; }

    public Audience adventure() { return getServer(); }
    public Audience adventure(Player player) { return player; }
}