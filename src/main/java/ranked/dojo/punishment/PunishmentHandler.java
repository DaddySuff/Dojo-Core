package ranked.dojo.punishment;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import ranked.dojo.Dojo;
import ranked.dojo.util.CC;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author curxxed
 * @date 6/28/2025
 * @project Dojo
 */
public class PunishmentHandler implements Listener {
    // --- Ban Logic ---
    private static final BanManager banManager = new BanManager();

    /**
     * Handles all ban-related logic and storage.
     */
    public static class BanManager {
        // UUID -> unban timestamp (ms), -1L for perm
        private final Map<UUID, Long> bannedPlayers = new HashMap<>();

        /**
         * Ban a player for a given duration (in ms). Use -1L for permanent.
         */
        public void banPlayer(UUID uuid, long durationMillis) {
            bannedPlayers.put(uuid, durationMillis == -1L ? -1L : System.currentTimeMillis() + durationMillis);
        }

        /**
         * Check if a player is currently banned.
         */
        public boolean isBanned(UUID uuid) {
            if (!bannedPlayers.containsKey(uuid)) return false;
            long expiry = bannedPlayers.get(uuid);
            if (expiry == -1L) return true;
            if (System.currentTimeMillis() > expiry) {
                bannedPlayers.remove(uuid);
                return false;
            }
            return true;
        }

        /**
         * Get the ban expiry timestamp for a player (0L if not banned).
         */
        public long getBanExpiry(UUID uuid) {
            return bannedPlayers.getOrDefault(uuid, 0L);
        }

        /**
         * Unban a player.
         */
        public void unbanPlayer(UUID uuid) {
            bannedPlayers.remove(uuid);
        }
    }

    // --- Ban API for use elsewhere ---
    public static void banPlayer(UUID uuid, long durationMillis) {
        banManager.banPlayer(uuid, durationMillis);
    }
    public static boolean isBanned(UUID uuid) {
        return banManager.isBanned(uuid);
    }
    public static long getBanExpiry(UUID uuid) {
        return banManager.getBanExpiry(uuid);
    }
    public static void unbanPlayer(UUID uuid) {
        banManager.unbanPlayer(uuid);
    }

    // --- Punishment Logic ---
    private static final Map<UUID, List<Punishment>> punishments = new HashMap<>();
    private static final String PUNISHMENTS_FILE = "plugins/Dojo/punishments.json";
    public static void loadPunishments() {
        File file = new File(PUNISHMENTS_FILE);
        if (!file.exists()) return;
        boolean changed = false;
        try {
            String json = new String(Files.readAllBytes(Paths.get(PUNISHMENTS_FILE)));
            Map<String, List<String>> map = new Gson().fromJson(json, Map.class);
            if (map != null) {
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    UUID uuid = UUID.fromString(entry.getKey());
                    List<Punishment> loaded = PunishmentSerializer.deserialize(entry.getValue());
                    // Check for expired punishments
                    for (Punishment p : loaded) {
                        if (p.isActive() && !p.isPermanent() && p.getExpiration() > 0 && System.currentTimeMillis() > p.getExpiration()) {
                            p.setActive(false);
                            p.setRemovedAt(p.getExpiration());
                            p.setPardoner("CONSOLE");
                            p.setPardonReason("Expired");
                            changed = true;
                        }
                    }
                    punishments.put(uuid, loaded);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Re-apply all active bans to BanManager
        for (Map.Entry<UUID, List<Punishment>> entry : punishments.entrySet()) {
            UUID uuid = entry.getKey();
            for (Punishment p : entry.getValue()) {
                if (p.isActive() && p.getPunishmentType() != null && p.getPunishmentType().name().equalsIgnoreCase("BAN")) {
                    // Only re-apply if not expired
                    long remaining = p.isPermanent() ? -1L : (p.getExpiration() - System.currentTimeMillis());
                    if (p.isPermanent() || remaining > 0) {
                        banManager.banPlayer(uuid, remaining);
                    }
                }
            }
        }
        if (changed) savePunishments();
    }
    public static void savePunishments() {
        Map<String, List<String>> map = new HashMap<>();
        for (Map.Entry<UUID, List<Punishment>> entry : punishments.entrySet()) {
            map.put(entry.getKey().toString(), PunishmentSerializer.serialize(entry.getValue()));
        }
        String json = new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(map);
        try {
            File file = new File(PUNISHMENTS_FILE);
            file.getParentFile().mkdirs();
            Files.write(Paths.get(PUNISHMENTS_FILE), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a punishment for a player.
     */
    public static void addPunishment(UUID uuid, Punishment punishment) {
        punishments.computeIfAbsent(uuid, k -> new java.util.ArrayList<>()).add(punishment);
        savePunishments();
    }

    // Call this after updating/removing a punishment
    public static void updatePunishments(UUID uuid) {
        savePunishments();
    }

    /**
     * Get all punishments for a player.
     */
    public static List<Punishment> getPunishments(UUID uuid) {
        return punishments.getOrDefault(uuid, java.util.Collections.emptyList());
    }

    /**
     * Get active punishments for a player.
     */
    public static List<Punishment> getActivePunishments(UUID uuid) {
        List<Punishment> all = getPunishments(uuid);
        List<Punishment> active = new java.util.ArrayList<>();
        for (Punishment p : all) {
            if (p.isActive()) active.add(p);
        }
        return active;
    }

    /**
     * Get inactive punishments for a player.
     */
    public static List<Punishment> getInactivePunishments(UUID uuid) {
        List<Punishment> all = getPunishments(uuid);
        List<Punishment> inactive = new java.util.ArrayList<>();
        for (Punishment p : all) {
            if (!p.isActive()) inactive.add(p);
        }
        return inactive;
    }

    // Call this in onEnable
    public static void startExpirationTask(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            boolean changed = false;
            long now = System.currentTimeMillis();
            for (Map.Entry<UUID, List<Punishment>> entry : punishments.entrySet()) {
                for (Punishment p : entry.getValue()) {
                    if (p.isActive() && !p.isPermanent() && p.getExpiration() > 0 && now > p.getExpiration()) {
                        p.setActive(false);
                        p.setRemovedAt(p.getExpiration());
                        p.setPardoner("CONSOLE");
                        p.setPardonReason("Expired");
                        changed = true;
                    }
                }
            }
            if (changed) savePunishments();
        }, 20L, 20L * 60); // every 60 seconds
    }

    // --- Ban Enforcement Listeners ---
    /**
     * Prevent banned players from using commands.
     */
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (isBanned(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&c&lYou cannot execute commands while banned!\n"));
        }
    }

    /**
     * Remind banned players of their ban when they join.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (isBanned(player.getUniqueId())) {
            long expiry = getBanExpiry(player.getUniqueId());
            String durationString;
            if (expiry == -1L) {
                durationString = "Permanent";
            } else {
                long millisLeft = expiry - System.currentTimeMillis();
                if (millisLeft < 0) millisLeft = 0;
                long days = millisLeft / (1000 * 60 * 60 * 24);
                millisLeft %= (1000 * 60 * 60 * 24);
                long hours = millisLeft / (1000 * 60 * 60);
                millisLeft %= (1000 * 60 * 60);
                long minutes = millisLeft / (1000 * 60);
                millisLeft %= (1000 * 60);
                long seconds = millisLeft / 1000;
                StringBuilder sb = new StringBuilder();
                if (days > 0) sb.append(days).append(" Day").append(days > 1 ? "s " : " ");
                if (hours > 0) sb.append(hours).append(" Hour").append(hours > 1 ? "s " : " ");
                if (minutes > 0) sb.append(minutes).append(" Minute").append(minutes > 1 ? "s " : " ");
                if (seconds > 0 && days == 0 && hours == 0) sb.append(seconds).append(" Second").append(seconds > 1 ? "s" : "");
                durationString = sb.toString().trim();
                if (durationString.isEmpty()) durationString = "0 Seconds";
            }
            String banReason = "Unknown";
            List<Punishment> activePunishments = getActivePunishments(player.getUniqueId());
            for (Punishment p : activePunishments) {
                if (p.getPunishmentType() != null && p.getPunishmentType().name().equalsIgnoreCase("BAN")) {
                    banReason = p.getPunishReason();
                    break;
                }
            }
            String banMsg = "&cBANNED FOR " + durationString + "\n" +
                    "&7Reason: &f" + banReason + "\n" +
                    "&7Appeal on our discord &b&lrankeddojo.gg";
            Bukkit.getScheduler().runTaskLater(Dojo.getInstance(), () -> {
                player.sendMessage(CC.translate(banMsg));
            }, 20L);
        }
    }
}
