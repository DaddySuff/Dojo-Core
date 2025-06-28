package ranked.dojo.command.impl.donator;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.Dojo;
import ranked.dojo.profile.Profile;
import ranked.dojo.util.BukkitUtils;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

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

        for (Player players : BukkitUtils.getOnlinePlayers()) {
            broadcastMessage.forEach(line -> players.sendMessage(CC.translate(line)));
        }
    }
}