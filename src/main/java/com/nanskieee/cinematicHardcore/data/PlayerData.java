package com.nanskieee.cinematicHardcore.data;

import org.bukkit.inventory.ItemStack;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private String playerName;
    private boolean completedIntro;
    private boolean hardcoreDead;

    private boolean isInCinematic;
    private ItemStack[] savedInventory;
    private ItemStack[] savedArmor;

    // Fitur Bahasa
    private String language;

    public PlayerData(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.completedIntro = false;
        this.hardcoreDead = false;
        this.isInCinematic = false;
        this.language = "id"; // Default Bahasa Indonesia
    }

    public UUID getUuid() { return uuid; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public boolean hasCompletedIntro() { return completedIntro; }
    public void setCompletedIntro(boolean completedIntro) { this.completedIntro = completedIntro; }

    public boolean isHardcoreDead() { return hardcoreDead; }
    public void setHardcoreDead(boolean hardcoreDead) { this.hardcoreDead = hardcoreDead; }

    public boolean isInCinematic() { return isInCinematic; }
    public void setInCinematic(boolean inCinematic) { this.isInCinematic = inCinematic; }

    public ItemStack[] getSavedInventory() { return savedInventory; }
    public void setSavedInventory(ItemStack[] savedInventory) { this.savedInventory = savedInventory; }

    public ItemStack[] getSavedArmor() { return savedArmor; }
    public void setSavedArmor(ItemStack[] savedArmor) { this.savedArmor = savedArmor; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}