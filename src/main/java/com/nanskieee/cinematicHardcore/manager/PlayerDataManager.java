package com.nanskieee.cinematicHardcore.manager;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import com.nanskieee.cinematicHardcore.data.PlayerData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private final CinematicHardcore plugin;
    private final Map<UUID, PlayerData> cache = new ConcurrentHashMap<>();
    private final File dataFolder;

    public PlayerDataManager(CinematicHardcore plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public PlayerData get(UUID uuid) {
        return cache.computeIfAbsent(uuid, this::load);
    }

    private PlayerData load(UUID uuid) {
        File file = new File(dataFolder, uuid + ".yml");
        PlayerData data;
        if (file.exists()) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            data = new PlayerData(uuid, yaml.getString("name", "Unknown"));
            data.setCompletedIntro(yaml.getBoolean("completed-intro", false));
            data.setHardcoreDead(yaml.getBoolean("hardcore-dead", false));
            data.setInCinematic(yaml.getBoolean("in-cinematic", false));
            data.setLanguage(yaml.getString("language", "id"));

            if (yaml.contains("inv")) data.setSavedInventory(yaml.getList("inv").toArray(new ItemStack[0]));
            if (yaml.contains("armor")) data.setSavedArmor(yaml.getList("armor").toArray(new ItemStack[0]));
        } else {
            data = new PlayerData(uuid, "Unknown");
        }
        return data;
    }

    public void save(UUID uuid) {
        PlayerData data = cache.get(uuid);
        if (data == null) return;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            File file = new File(dataFolder, uuid + ".yml");
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.set("name", data.getPlayerName());
            yaml.set("completed-intro", data.hasCompletedIntro());
            yaml.set("hardcore-dead", data.isHardcoreDead());
            yaml.set("in-cinematic", data.isInCinematic());
            yaml.set("language", data.getLanguage());
            yaml.set("inv", data.getSavedInventory());
            yaml.set("armor", data.getSavedArmor());
            try {
                yaml.save(file);
            } catch (IOException e) {
                plugin.getLogger().severe("Gagal menyimpan data: " + e.getMessage());
            }
        });
    }
}