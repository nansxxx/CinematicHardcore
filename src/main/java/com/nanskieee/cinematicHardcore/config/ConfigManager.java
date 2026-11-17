package com.nanskieee.cinematicHardcore.config;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final CinematicHardcore plugin;
    private FileConfiguration config;

    public ConfigManager(CinematicHardcore plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public FileConfiguration get() { return config; }

    public String getString(String path, String def) { return config.getString(path, def); }
    public int getInt(String path, int def) { return config.getInt(path, def); }
    public boolean getBoolean(String path, boolean def) { return config.getBoolean(path, def); }
    public double getDouble(String path, double def) { return config.getDouble(path, def); }

    public boolean sceneEnabled(String scene) {
        return config.getBoolean("cinematic." + scene + ".enabled", true);
    }

    public int sceneDurationTicks(String scene) {
        double seconds = config.getDouble("cinematic." + scene + ".duration", 4);
        return (int) Math.round(seconds * 20);
    }

    public String sceneTitle(String scene) { return config.getString("cinematic." + scene + ".title", ""); }
    public String sceneSubtitle(String scene) { return config.getString("cinematic." + scene + ".subtitle", ""); }
}