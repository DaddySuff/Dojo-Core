package me.suff.dojo.rank.command.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.rank.Rank;
import me.suff.dojo.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 30/08/2024 - 22:48
 */
public class RankSetBoldCommand extends BaseCommand {
    @Command(name = "rank.setbold", permission = "dojo.rank.setbold", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: &e/rank setbold (name) (true/false)"));
            return;
        }

        String name = args[0];
        Rank rank = Dojo.getInstance().getRankService().getRank(name);
        if (rank == null) {
            sender.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        boolean bold;

        switch (args[1]) {
            case "true":
                bold = true;
                break;
            case "false":
                bold = false;
                break;
            default:
                sender.sendMessage(CC.translate("&cInvalid boolean value. Please use 'true' or 'false'."));
                return;
        }

        rank.setBold(bold);
        Dojo.getInstance().getRankService().saveRank(rank);
        sender.sendMessage(CC.translate("&aSuccessfully set the bold of &e" + name + " &ato &6" + bold + "&a."));
    }
}
