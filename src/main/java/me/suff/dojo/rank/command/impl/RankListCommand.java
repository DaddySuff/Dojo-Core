package me.suff.dojo.rank.command.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.rank.Rank;
import me.suff.dojo.rank.RankService;
import me.suff.dojo.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Artex
 * @date 28/08/2024 - 22:13
 */
public class RankListCommand extends BaseCommand {
    @Command(name = "rank.list", permission = "dojo.rank.list", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lDojo &8- &7Ranks"));
        sender.sendMessage("");

        RankService rankService = Dojo.getInstance().getRankService();
        if (rankService.getRanks() == null || rankService.getRanks().isEmpty()) {
            sender.sendMessage(CC.translate("&6&l● &cNo ranks found."));
            sender.sendMessage("");
            return;
        }

        List<Rank> sortedRanks = rankService.getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .collect(Collectors.toList());

        for (Rank rank : sortedRanks) {
            sender.sendMessage(CC.translate("&6&l● " + rank.getColor() + rank.getName() + " &7- &f" + rank.getPrefix() + "&8[&7" + rank.getWeight() + "&8]" + (rank.isDefaultRank() ? " &7&o(Default Rank)" : "")));
        }
        sender.sendMessage("");

    }
}
