package com.nanskieee.cinematicHardcore.command;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import com.nanskieee.cinematicHardcore.gui.LanguageGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChangeLangCommand implements CommandExecutor {
    private final CinematicHardcore plugin;

    public ChangeLangCommand(CinematicHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Hanya pemain yang bisa menggunakan perintah ini.");
            return true;
        }

        new LanguageGUI(plugin).open(player);
        return true;
    }
}