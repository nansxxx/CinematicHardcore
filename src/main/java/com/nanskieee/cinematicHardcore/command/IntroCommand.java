package com.nanskieee.cinematicHardcore.command;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IntroCommand implements CommandExecutor, TabCompleter {
    private final CinematicHardcore plugin;

    public IntroCommand(CinematicHardcore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player p) {
                plugin.getCinematicManager().start(p);
                sender.sendMessage("Memulai cinematic...");
            } else {
                sender.sendMessage("Gunakan: /intro <start|skip|reset|reload> [player]");
            }
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("reload")) {
            plugin.getConfigManager().reload();
            sender.sendMessage("§aConfig berhasil di-reload!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cHarap masukkan nama pemain!");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cPemain tidak ditemukan!");
            return true;
        }

        switch (sub) {
            case "start" -> {
                plugin.getCinematicManager().start(target);
                sender.sendMessage("§aMemulai cinematic untuk " + target.getName());
            }
            case "skip" -> {
                plugin.getCinematicManager().skip(target);
                sender.sendMessage("§aMelewati cinematic untuk " + target.getName());
            }
            case "reset" -> {
                plugin.getPlayerDataManager().get(target.getUniqueId()).setCompletedIntro(false);
                plugin.getPlayerDataManager().save(target.getUniqueId());
                sender.sendMessage("§aStatus intro " + target.getName() + " telah di-reset.");
            }
            case "status" -> {
                boolean done = plugin.getPlayerDataManager().get(target.getUniqueId()).hasCompletedIntro();
                sender.sendMessage("§eStatus Intro " + target.getName() + ": §f" + (done ? "Selesai" : "Belum"));
            }
            default -> sender.sendMessage("§cSubcommand tidak dikenal!");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("start");
            completions.add("skip");
            completions.add("reset");
            completions.add("reload");
            completions.add("status");
        } else if (args.length == 2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                completions.add(p.getName());
            }
        }
        return completions;
    }
}