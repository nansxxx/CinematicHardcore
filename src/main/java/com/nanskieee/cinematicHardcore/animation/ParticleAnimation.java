package com.nanskieee.cinematicHardcore.animation;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitTask;

public class ParticleAnimation {
    private final CinematicHardcore plugin;
    private BukkitTask task;

    public ParticleAnimation(CinematicHardcore plugin) {
        this.plugin = plugin;
    }

    public void startAmbient(Location base, Particle particle, int intervalTicks, int count) {
        stop();
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (base.getWorld() == null) return;
            double dx = (Math.random() - 0.5) * 3;
            double dy = Math.random() * 2.5;
            double dz = (Math.random() - 0.5) * 3;
            base.getWorld().spawnParticle(particle, base.clone().add(dx, dy, dz), count, 0.1, 0.1, 0.1, 0.02);
        }, 0L, intervalTicks);
    }

    public void stop() {
        if (task != null) { task.cancel(); task = null; }
    }
}