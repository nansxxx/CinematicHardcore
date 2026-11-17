package com.nanskieee.cinematicHardcore.command;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import com.nanskieee.cinematicHardcore.utils.TextFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GuideCommand implements CommandExecutor {
    private final CinematicHardcore plugin;

    public GuideCommand(CinematicHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Perintah ini hanya bisa digunakan oleh player.");
            return true;
        }

        // Mengambil dari config.yml
        List<String> lines = plugin.getConfigManager().get().getStringList("guide.lines");

        if (lines.isEmpty()) {
            player.sendMessage(TextFormatter.parse("<red>Panduan belum diatur di config.yml!</red>"));
            return true;
        }

        for (String line : lines) {
            plugin.adventure(player).sendMessage(TextFormatter.parseForPlayer(line, player));
        }

        return true;
    }
}