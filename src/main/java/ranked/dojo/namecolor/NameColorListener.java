package ranked.dojo.namecolor;

import ranked.dojo.Dojo;
import ranked.dojo.namecolor.integration.RankIntegration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NameColorListener implements Listener {
    private final Dojo plugin;
    private final RankIntegration rankIntegration;

    public NameColorListener(Dojo plugin, RankIntegration rankIntegration) {
        this.plugin = plugin;
        this.rankIntegration = rankIntegration;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // No need for manual scheduling - refreshPlayerFormatting handles it
        rankIntegration.refreshPlayerFormatting(event.getPlayer());
    }
}