package me.suff.dojo.namecolor;

import me.suff.dojo.namecolor.integration.RankIntegration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final RankIntegration rankIntegration;

    public PlayerJoinListener(RankIntegration rankIntegration) {
        this.rankIntegration = rankIntegration;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        rankIntegration.refreshPlayerFormatting(event.getPlayer());
    }
}
