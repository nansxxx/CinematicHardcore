package com.nanskieee.cinematicHardcore.manager;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
    private final CinematicHardcore plugin;
    private final Map<String, FileConfiguration> languages = new HashMap<>();

    public LanguageManager(CinematicHardcore plugin) {
        this.plugin = plugin;
        loadLanguage("id");
        loadLanguage("en");
    }

    private void loadLanguage(String langCode) {
        File file = new File(plugin.getDataFolder(), "lang/" + langCode + ".yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("lang/" + langCode + ".yml", false);
        }
        languages.put(langCode, YamlConfiguration.loadConfiguration(file));
    }

    public String getMessage(String langCode, String path) {
        FileConfiguration config = languages.getOrDefault(langCode, languages.get("id"));
        return config.getString(path, "Missing message: " + path);
    }

    // Untuk perubahan config saat runtime (jika ada /reload)
    public void reload() {
        languages.clear();
        loadLanguage("id");
        loadLanguage("en");
    }
}