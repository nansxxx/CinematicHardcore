package com.nanskieee.cinematicHardcore.manager;

import com.nanskieee.cinematicHardcore.CinematicHardcore;
import com.nanskieee.cinematicHardcore.animation.FloatingTotem;
import com.nanskieee.cinematicHardcore.animation.ParticleAnimation;
import com.nanskieee.cinematicHardcore.animation.ScreenTransition;
import com.nanskieee.cinematicHardcore.config.ConfigManager;
import com.nanskieee.cinematicHardcore.data.PlayerData;
import com.nanskieee.cinematicHardcore.utils.SoundUtils;
import com.nanskieee.cinematicHardcore.utils.TextFormatter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CinematicManager {
    private final CinematicHardcore plugin;
    private final ConfigManager config;
    private final DeploymentManager deploymentManager;
    private final PlayerDataManager dataManager;
    private final ParticleAnimation particleAnimation;
    private final ScreenTransition screenTransition;
    private final Map<UUID, Session> sessions = new ConcurrentHashMap<>();

    private static class Session {
        Player player;
        GameMode originalGameMode;
        BukkitTask task;
        FloatingTotem totemLeft;
        FloatingTotem totemRight;
        BossBar bossBar; // Fitur Baru: BossBar
        boolean cancelled;
        Location finalLocation;
    }

    public CinematicManager(CinematicHardcore plugin, ConfigManager config, DeploymentManager deploymentManager, PlayerDataManager dataManager) {
        this.plugin = plugin;
        this.config = config;
        this.deploymentManager = deploymentManager;
        this.dataManager = dataManager;
        this.particleAnimation = new ParticleAnimation(plugin);
        this.screenTransition = new ScreenTransition();
    }

    public boolean isInCinematic(Player player) {
        return sessions.containsKey(player.getUniqueId());
    }

    public void start(Player player) {
        if (sessions.containsKey(player.getUniqueId())) return;

        PlayerData data = dataManager.get(player.getUniqueId());

        // Jika player crash saat cinematic, pulihkan inventory dan lewati cinematic
        if (data.isInCinematic()) {
            plugin.getLogger().warning("Memulihkan player " + player.getName() + " dari crash cinematic.");
            restoreState(player, data);
            return;
        }

        Session session = new Session();
        session.player = player;
        session.originalGameMode = player.getGameMode();
        sessions.put(player.getUniqueId(), session);

        // SIMPAN INVENTORY (Anti Hilang)
        data.setSavedInventory(player.getInventory().getContents().clone());
        data.setSavedArmor(player.getInventory().getArmorContents().clone());
        data.setInCinematic(true);
        dataManager.save(player.getUniqueId());

        // Proteksi Player
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setFallDistance(0f);

        runSequence(session, 0);
    }

    public void skip(Player player) {
        Session s = sessions.get(player.getUniqueId());
        if (s == null) return;
        s.cancelled = true;
        if (s.task != null) { s.task.cancel(); s.task = null; }
        finishAndCleanup(s);
    }

    private void runSequence(Session session, int scene) {
        if (session.cancelled || !session.player.isOnline()) return;

        // ASYNC CHUNK LOADING PREPARATION
        if (scene == 4) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                Optional<Location> safeLoc = deploymentManager.findSafeLocation(session.player);
                session.finalLocation = safeLoc.orElse(session.player.getWorld().getSpawnLocation());
            });
        }

        switch (scene) {
            case 0 -> runOpening(session);
            case 1 -> runWelcome(session);
            case 2 -> runGuide(session);
            case 3 -> runHardcoreWarning(session);
            case 4 -> runEnterWorld(session);
            case 5 -> runPreDeploy(session);
            default -> finishDeployment(session);
        }
    }

    private void scheduleNext(Session session, int nextScene, int delayTicks) {
        session.task = plugin.getServer().getScheduler().runTaskLater(plugin,
                () -> runSequence(session, nextScene), delayTicks);
    }

    // SCENE 2
    private void runOpening(Session session) {
        Player p = session.player;
        int totalTicks = config.sceneDurationTicks("opening");
        String titleStr = config.sceneTitle("opening");
        String subStr = config.sceneSubtitle("opening");
        String sound = config.getString("cinematic.opening.sound", "BLOCK_NOTE_BLOCK_CHIME");

        for (int i = 0; i < 3; i++) {
            int finalI = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (session.cancelled) return;
                p.showTitle(Title.title(
                        TextFormatter.parseForPlayer(titleStr, p),
                        TextFormatter.parseForPlayer(subStr.replace("{countdown}", String.valueOf(3 - finalI)), p),
                        Title.Times.times(Duration.ofMillis(100), Duration.ofMillis(1000), Duration.ofMillis(500))
                ));
                SoundUtils.play(p, sound, 1f, 1f + (finalI * 0.1f));
            }, (long) finalI * 20);
        }
        scheduleNext(session, 1, totalTicks);
    }

    // SCENE 3
    private void runWelcome(Session session) {
        Player p = session.player;
        int totalTicks = config.sceneDurationTicks("welcome");
        p.showTitle(Title.title(
                TextFormatter.parseForPlayer(config.sceneTitle("welcome"), p),
                TextFormatter.parseForPlayer(config.sceneSubtitle("welcome"), p),
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(2), Duration.ofMillis(500))
        ));
        SoundUtils.play(p, "ENTITY_PLAYER_LEVELUP", 1f, 1f);

        Location loc = p.getLocation();
        session.totemLeft = new FloatingTotem(plugin, p.getUniqueId(), loc.clone().add(2, 1.5, 0));
        session.totemRight = new FloatingTotem(plugin, p.getUniqueId(), loc.clone().subtract(2, -1.5, 0));
        session.totemLeft.spawn();
        session.totemRight.spawn();
        scheduleNext(session, 2, totalTicks);
    }

    // SCENE 4
    private void runGuide(Session session) {
        Player p = session.player;
        int totalTicks = config.sceneDurationTicks("guide");
        p.showTitle(Title.title(
                TextFormatter.parseForPlayer(config.sceneTitle("guide"), p),
                TextFormatter.parseForPlayer(config.sceneSubtitle("guide"), p),
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(2), Duration.ofMillis(500))
        ));
        SoundUtils.play(p, "BLOCK_NOTE_BLOCK_BELL", 1f, 1.2f);
        scheduleNext(session, 3, totalTicks);
    }

    // SCENE 5
    private void runHardcoreWarning(Session session) {
        Player p = session.player;
        int totalTicks = config.sceneDurationTicks("hardcore");
        p.showTitle(Title.title(
                TextFormatter.parseForPlayer(config.sceneTitle("hardcore"), p),
                TextFormatter.parseForPlayer(config.sceneSubtitle("hardcore"), p),
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(2), Duration.ofMillis(500))
        ));
        SoundUtils.play(p, "ENTITY_WITHER_SPAWN", 0.8f, 1.5f);
        scheduleNext(session, 4, totalTicks);
    }

    // SCENE 6
    private void runEnterWorld(Session session) {
        Player p = session.player;
        int totalTicks = config.sceneDurationTicks("enter");
        p.showTitle(Title.title(
                TextFormatter.parseForPlayer(config.sceneTitle("enter"), p),
                TextFormatter.parseForPlayer(config.sceneSubtitle("enter"), p),
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(2), Duration.ofMillis(500))
        ));
        screenTransition.fadeIn(p, 40);
        screenTransition.shakeCamera(plugin, p, 20); // Efek Getar
        SoundUtils.play(p, "BLOCK_PORTAL_TRAVEL", 1f, 0.8f);
        scheduleNext(session, 5, totalTicks);
    }

    // SCENE 7: BOSS BAR COUNTDOWN
    private void runPreDeploy(Session session) {
        Player p = session.player;
        int countdownSec = config.getInt("cinematic.deployment.countdown", 3);

        // Buat BossBar
        session.bossBar = BossBar.bossBar(
                TextFormatter.parse("<red><bold>BERSIAP TERJUN: " + countdownSec + "</bold></red>"),
                1.0f, BossBar.Color.RED, BossBar.Overlay.PROGRESS
        );
        p.showBossBar(session.bossBar);

        for (int i = countdownSec; i >= 1; i--) {
            int finalI = i;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (session.cancelled) return;

                // Update Title
                p.showTitle(Title.title(
                        TextFormatter.parse("<red><bold>BERSIAP TERJUN</bold></red>"),
                        TextFormatter.parse("<white>Timer: <red>" + finalI + "</red></white>"),
                        Title.Times.times(Duration.ofMillis(100), Duration.ofMillis(1000), Duration.ofMillis(200))
                ));

                // Update BossBar
                session.bossBar.name(TextFormatter.parse("<red><bold>BERSIAP TERJUN: " + finalI + "</bold></red>"));
                session.bossBar.progress((float) finalI / countdownSec);

                SoundUtils.play(p, "BLOCK_NOTE_BLOCK_PLING", 1f, 0.5f + ((float) finalI / 10));
            }, (long) (countdownSec - i) * 20);
        }
        scheduleNext(session, 6, countdownSec * 20);
    }

    // SCENE 8
    private void finishDeployment(Session session) {
        Player p = session.player;
        if (session.finalLocation != null) {
            deploymentManager.deploy(p, session.finalLocation);
        }
        p.showTitle(Title.title(
                TextFormatter.parse("<green><bold>Kamu telah diturunkan!</bold></green>"),
                TextFormatter.parse("<white>Bertahanlah hidup!</white>"),
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(1000))
        ));
        SoundUtils.play(p, "ENTITY_PLAYER_LEVELUP", 1f, 0.8f);
        finishAndCleanup(session);
    }

    private void finishAndCleanup(Session session) {
        Player p = session.player;
        if (session.task != null) session.task.cancel();
        if (session.totemLeft != null) session.totemLeft.remove();
        if (session.totemRight != null) session.totemRight.remove();
        if (session.bossBar != null) p.hideBossBar(session.bossBar); // Hapus BossBar
        particleAnimation.stop();
        screenTransition.clearTransitions(p);

        if (p.isOnline()) {
            restoreState(p, dataManager.get(p.getUniqueId()));
        }
        sessions.remove(p.getUniqueId());
    }

    // FUNGSI PEMULIHAN STATE
    private void restoreState(Player p, PlayerData data) {
        p.setGameMode(GameMode.SURVIVAL);
        p.setInvulnerable(false);
        p.setAllowFlight(false);

        if (data.getSavedInventory() != null) {
            p.getInventory().setContents(data.getSavedInventory());
            data.setSavedInventory(null);
        }
        if (data.getSavedArmor() != null) {
            p.getInventory().setArmorContents(data.getSavedArmor());
            data.setSavedArmor(null);
        }

        data.setInCinematic(false);
        data.setCompletedIntro(true);
        dataManager.save(p.getUniqueId());
    }
}