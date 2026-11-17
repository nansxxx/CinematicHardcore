package com.nanskieee.cinematicHardcore.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public final class LocationValidator {
    private LocationValidator() {}

    public static boolean isSafe(Location loc, World world) {
        if (loc == null || world == null) return false;
        if (!loc.getWorld().equals(world)) return false;
        if (!world.getWorldBorder().isInside(loc)) return false;

        int y = loc.getBlockY();
        if (y <= world.getMinHeight() + 2 || y >= world.getMaxHeight() - 3) return false;

        Block below = loc.getBlock().getRelative(0, -1, 0);
        Block feet = loc.getBlock();
        Block head = loc.getBlock().getRelative(0, 1, 0);

        if (!below.getType().isSolid()) return false;
        if (feet.isLiquid() || head.isLiquid()) return false;
        if (!isPassable(feet.getType()) || !isPassable(head.getType())) return false;
        if (below.getType() == Material.MAGMA_BLOCK || below.getType() == Material.CACTUS) return false;

        return true;
    }

    private static boolean isPassable(Material m) {
        return m.isAir() || m == Material.SHORT_GRASS || m == Material.TALL_GRASS
                || m == Material.FERN || m == Material.LARGE_FERN || m == Material.SNOW;
    }
}