package com.nanskieee.cinematicHardcore.animation;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class ScreenTransition {
    private BukkitTask shakeTask;

    public void fadeIn(Player player, int durationTicks) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, durationTicks, 0, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, durationTicks / 2, 0, false, false, false));
        player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 0.6f, 0.6f);
    }

    // Efek Kamera Bergetar (Camera Shake)
    public void shakeCamera(CinematicHardcore plugin, Player player, int durationTicks) {
        if (shakeTask != null) shakeTask.cancel();

        shakeTask = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= durationTicks || !player.isOnline()) {
                    shakeTask.cancel();
                    return;
                }
                Location eyeLoc = player.getEyeLocation();
                Vector dir = eyeLoc.getDirection();
                Location front = eyeLoc.clone().add(dir.multiply(1.5));

                // Memunculkan partikel acak di depan mata untuk efek getar
                double offX = (Math.random() - 0.5) * 0.5;
                double offY = (Math.random() - 0.5) * 0.5;
                double offZ = (Math.random() - 0.5) * 0.5;

                player.getWorld().spawnParticle(Particle.SMOKE, front.add(offX, offY, offZ), 1, 0, 0, 0, 0);
                ticks++;
            }
        }, 0L, 1L);
    }

    public void clearTransitions(Player player) {
        player.removePotionEffect(PotionEffectType.DARKNESS);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
    }
}