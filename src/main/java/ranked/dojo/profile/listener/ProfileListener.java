package ranked.dojo.profile.listener;

import ranked.dojo.Dojo;
import ranked.dojo.profile.Profile;
import ranked.dojo.util.PlayerUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class ProfileListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Profile profile = new Profile(player.getUniqueId());
        profile.load();

        Dojo.getInstance().getProfileRepository().addProfile(profile.getUuid(), profile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = Dojo.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setUsername(player.getName());

        profile.determineRankAndAttachPerms();

        FileConfiguration config = Dojo.getInstance().getConfig();
        if (config.getBoolean("on-join.tp-to-spawn")) {
            Dojo.getInstance().getSpawnHandler().teleportToSpawn(player);
        }

        event.setJoinMessage(null);

        if (config.getBoolean("on-join.reset-player", false)) {
            PlayerUtil.onJoinReset(player);
        }

        PlayerUtil.sendWelcomeMessage(player, profile, config);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Dojo.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.save();

        event.setQuitMessage(null);
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = Dojo.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.save();
    }
}
