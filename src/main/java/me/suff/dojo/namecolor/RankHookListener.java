package me.suff.dojo.namecolor;

import me.suff.dojo.Dojo;
import me.suff.dojo.namecolor.integration.RankIntegration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RankHookListener implements Listener {
    private final Dojo plugin;
    private final RankIntegration rankIntegration;
    private final Set<String> rankCommands = new HashSet<>(Arrays.asList(
            "/rank", "/setrank", "/addrank", "/delrank", "/removerank",
            "/promote", "/demote", "/createrank", "/deleterank"
    ));

    public RankHookListener(Dojo plugin, RankIntegration rankIntegration) {
        this.plugin = plugin;
        this.rankIntegration = rankIntegration;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRankCommand(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().toLowerCase().split(" ");
        if (args.length < 2 || !rankCommands.contains(args[0])) return;

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target != null) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                rankIntegration.refreshPlayerFormatting(target);
            }, 3L);
        }
    }
}