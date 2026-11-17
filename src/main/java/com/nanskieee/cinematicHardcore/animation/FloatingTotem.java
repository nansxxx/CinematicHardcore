package com.nanskieee.cinematicHardcore.animation;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.UUID;

public class FloatingTotem {
    private final CinematicHardcore plugin;
    private ItemDisplay display;
    private BukkitTask task;
    private final Location baseLocation;
    private final float phaseOffset;

    public FloatingTotem(CinematicHardcore plugin, UUID owner, Location base) {
        this.plugin = plugin;
        this.baseLocation = base.clone();
        this.phaseOffset = (float) (Math.random() * Math.PI * 2);
    }

    public void spawn() {
        if (baseLocation.getWorld() == null) return;
        display = baseLocation.getWorld().spawn(baseLocation, ItemDisplay.class, entity -> {
            entity.setItemStack(new ItemStack(Material.TOTEM_OF_UNDYING));
            entity.setBillboard(Display.Billboard.CENTER);
            entity.setGravity(false);
            entity.setInvulnerable(true);
            entity.setPersistent(false);
            applyTransform(entity, 0f);
        });
        startAnimation();
    }

    private void startAnimation() {
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (display == null || !display.isValid()) return;
            float ticks = (System.currentTimeMillis() % 100000) / 50f;
            applyTransform(display, ticks);
        }, 0L, 2L);
    }

    // ANIMASI 3D (ROTASI SEMUA SUMBU)
    private void applyTransform(ItemDisplay entity, float ticks) {
        float bob = (float) Math.sin((ticks * 0.15) + phaseOffset) * 0.15f;
        float yaw = (ticks * 3f + phaseOffset * 20f) % 360f;
        float pitch = (float) Math.sin(ticks * 0.1) * 15f; // Animasi naik turun 3D
        float roll = (float) Math.cos(ticks * 0.1) * 15f;  // Animasi miring 3D

        Vector3f translation = new Vector3f(0, bob, 0);
        Quaternionf leftRot = new Quaternionf().rotateY((float) Math.toRadians(yaw));
        Quaternionf rightRot = new Quaternionf().rotateX((float) Math.toRadians(pitch)).rotateZ((float) Math.toRadians(roll));
        Vector3f scaleVec = new Vector3f(2f, 2f, 2f);

        entity.setTransformation(new Transformation(translation, leftRot, scaleVec, rightRot));
    }

    public void remove() {
        if (task != null) { task.cancel(); task = null; }
        if (display != null) { display.remove(); display = null; }
    }
}