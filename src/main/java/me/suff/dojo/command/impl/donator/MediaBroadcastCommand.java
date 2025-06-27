package me.suff.dojo.command.impl.donator;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.profile.Profile;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Artex
 * @date 23/10/2024 - 12:00
 */
public class MediaBroadcastCommand extends BaseCommand {
    @Command(name = "mediabroadcast", aliases = {"mb"}, permission = "dojo.mediabroadcast")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&CUsage: &e/mediabroadcast <link>"));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        Profile profile = Dojo.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        List<String> broadcastMessage = Arrays.asList(
                "",
                "&6&l" + profile.getHighestRankBasedOnGrant().getColor() + command.getPlayer().getName() + " &fis currently live!",
                "",
                " &7» &e" + message,
                ""
        );

        for (Player players : Bukkit.getOnlinePlayers()) {
            broadcastMessage.forEach(line -> players.sendMessage(CC.translate(line)));
        }
    }
}