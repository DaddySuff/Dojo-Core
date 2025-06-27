package me.suff.dojo.rank.command.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.rank.Rank;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 30/08/2024 - 22:51
 */
public class RankSetDefaultCommand extends BaseCommand {
    @Command(name = "rank.setdefault", permission = "dojo.rank.setdefault", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/rank setdefault (name)"));
            return;
        }

        if (Bukkit.getOnlinePlayers().size() > 1) {
            sender.sendMessage(CC.translate("&cYou can only set the default rank when no players are online."));
            return;
        }

        String name = args[0];
        Rank rank = Dojo.getInstance().getRankService().getRank(name);
        if (rank == null) {
            sender.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Dojo.getInstance().getRankService().getRanks().stream().filter(Rank::isDefaultRank).forEach(rank1 -> {
            rank1.setDefaultRank(false);
            Dojo.getInstance().getRankService().saveRank(rank1);
        });

        rank.setDefaultRank(true);
        Dojo.getInstance().getRankService().saveRank(rank);

        sender.sendMessage(CC.translate("&aSuccessfully set &e" + name + " &ato the default rank&a."));
    }
}
