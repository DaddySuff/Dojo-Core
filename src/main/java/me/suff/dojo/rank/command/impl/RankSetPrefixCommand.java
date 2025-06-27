package me.suff.dojo.rank.command.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.rank.Rank;
import me.suff.dojo.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Artex
 * @date 30/08/2024 - 22:42
 */
public class RankSetPrefixCommand extends BaseCommand {
    @Command(name = "rank.setprefix", permission = "dojo.rank.setprefix", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: &e/rank setprefix (name) (prefix)"));
            return;
        }

        String name = args[0];
        String prefix = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Rank rank = Dojo.getInstance().getRankService().getRank(name);
        if (rank == null) {
            sender.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        rank.setPrefix(prefix);
        Dojo.getInstance().getRankService().saveRank(rank);
        sender.sendMessage(CC.translate("&aSuccessfully set the prefix of &e" + name + " &ato &6" + prefix + "&a."));
    }
}
