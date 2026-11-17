package com.nanskieee.cinematicHardcore.manager;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import com.nanskieee.cinematicHardcore.config.ConfigManager;
import com.nanskieee.cinematicHardcore.utils.LocationValidator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class DeploymentManager {
    private final CinematicHardcore plugin;
    private final ConfigManager config;

    public DeploymentManager(CinematicHardcore plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;
    }

    public Optional<Location> findSafeLocation(Player player) {
        String worldName = config.getString("cinematic.deployment.world", "world");
        World world = Bukkit.getWorld(worldName);

        if (world == null) return fallbackLocation();

        int minD = config.getInt("cinematic.deployment.min-distance-from-spawn", 500);
        int maxD = config.getInt("cinematic.deployment.max-distance-from-spawn", 3000);
        int attempts = config.getInt("cinematic.deployment.max-attempts", 50);

        Location spawn = world.getSpawnLocation();

        for (int i = 0; i < attempts; i++) {
            double angle = ThreadLocalRandom.current().nextDouble(0, Math.PI * 2);
            int dist = ThreadLocalRandom.current().nextInt(minD, Math.max(minD + 1, maxD));
            int x = spawn.getBlockX() + (int) (Math.cos(angle) * dist);
            int z = spawn.getBlockZ() + (int) (Math.sin(angle) * dist);

            // Cek Bioma (Hindari Laut)
            Biome biome = world.getBiome(x, 64, z);
            if (biome == Biome.OCEAN || biome == Biome.DEEP_OCEAN || biome == Biome.FROZEN_OCEAN) continue;

            int y = world.getHighestBlockYAt(x, z);
            Location candidate = new Location(world, x + 0.5, y + 1, z + 0.5);

            if (LocationValidator.isSafe(candidate, world)) {
                return Optional.of(candidate);
            }
        }

        return fallbackLocation();
    }

    private Optional<Location> fallbackLocation() {
        String fbWorld = config.getString("cinematic.deployment.world", "world");
        World w = Bukkit.getWorld(fbWorld);
        return w != null ? Optional.of(w.getSpawnLocation()) : Optional.empty();
    }

    public void deploy(Player player, Location loc) {
        if (loc == null) return;
        player.teleport(loc);
        player.setFallDistance(0f);
    }
}