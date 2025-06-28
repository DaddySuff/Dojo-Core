package ranked.dojo.punishment;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import ranked.dojo.Dojo;
import ranked.dojo.util.CC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    // --- Ban Enforcement Listeners ---
    /**
     * Prevent banned players from using commands.
     */
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (isBanned(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("\n&8&m--------------------------------------\n" +
                    "&c&lYou are banned!\n" +
                    "&7You cannot use commands while banned.\n" +
                    "&8&m--------------------------------------\n"));
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
            String duration = expiry == -1L ? "Permanent" : "until " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(expiry));
            String banMsg = "\n&8&m--------------------------------------\n" +
                    "&c&lYou are currently banned!\n" +
                    "&6Duration: &f" + duration + "\n" +
                    "&7If you believe this is a mistake, contact staff.\n" +
                    "&8&m--------------------------------------\n";
            Bukkit.getScheduler().runTaskLater(Dojo.getInstance(), () -> {
                player.sendMessage(CC.translate(banMsg));
            }, 20L);
        }
    }
}
