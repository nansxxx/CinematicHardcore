package com.nanskieee.cinematicHardcore.gui;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import com.nanskieee.cinematicHardcore.data.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class LanguageGUI implements Listener {
    private final CinematicHardcore plugin;

    public LanguageGUI(CinematicHardcore plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, Component.text("Pilih Bahasa / Select Language"));

        inv.setItem(11, createItem(Material.RED_WOOL, "Bahasa Indonesia", "Klik untuk menggunakan Bahasa Indonesia"));
        inv.setItem(15, createItem(Material.BLUE_WOOL, "English", "Click to use English"));

        player.openInventory(inv);
    }

    private ItemStack createItem(Material mat, String name, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name).color(NamedTextColor.YELLOW));
            meta.lore(Arrays.asList(Component.text(lore).color(NamedTextColor.GRAY)));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(Component.text("Pilih Bahasa / Select Language"))) return;

        event.setCancelled(true); // Agar item tidak bisa diambil
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getCurrentItem() == null) return;

        PlayerData data = plugin.getPlayerDataManager().get(player.getUniqueId());

        if (event.getSlot() == 11) {
            data.setLanguage("id");
            player.sendMessage(Component.text("Bahasa berhasil diubah ke Indonesia!").color(NamedTextColor.GREEN));
            player.closeInventory();
        } else if (event.getSlot() == 15) {
            data.setLanguage("en");
            player.sendMessage(Component.text("Language successfully changed to English!").color(NamedTextColor.GREEN));
            player.closeInventory();
        }

        plugin.getPlayerDataManager().save(player.getUniqueId());
    }
}