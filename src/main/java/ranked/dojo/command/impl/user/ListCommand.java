package ranked.dojo.command.impl.user;

import ranked.dojo.Dojo;
import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.profile.Profile;
import ranked.dojo.rank.Rank;
import ranked.dojo.util.BukkitUtils;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class ListCommand extends BaseCommand {
    @Command(name = "list", aliases = "who", permission = "dojo.list", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String separator = "&f, &r";

        int onlinePlayers = BukkitUtils.getOnlinePlayers().size();
        int maxPlayers = Bukkit.getMaxPlayers();

        sender.sendMessage("");

        List<Rank> sortedRanks = Dojo.getInstance().getRankService().getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .collect(Collectors.toList());

        StringBuilder rankList = new StringBuilder();
        for (int i = 0; i < sortedRanks.size(); i++) {
            Rank rank = sortedRanks.get(i);
            rankList.append(CC.translate(rank.getColor() + rank.getName()));
            if (i < sortedRanks.size() - 1) {
                rankList.append(separator);
            }
        }

        StringBuilder playerList = new StringBuilder();
        for (Player player : BukkitUtils.getOnlinePlayers()) {
            Profile profile = Dojo.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            Rank playerRank = profile.getHighestRankBasedOnGrant();
            String playerName = player.getName();
            ChatColor rankColor = playerRank.getColor();

            playerList.append(rankColor).append(playerName).append(separator);
        }

        if (playerList.length() > 0) {
            playerList.setLength(playerList.length() - 2);
        }

        String playerListMessage = playerList.toString();
        if (onlinePlayers > 200) {
            playerListMessage = CC.translate("&7and " + (onlinePlayers - 200) + " more players");
        }

        sender.sendMessage(CC.translate(rankList.toString()));
        sender.sendMessage(CC.translate("&f(" + onlinePlayers + "/" + maxPlayers + "): " + playerListMessage));
        sender.sendMessage("");
    }
}