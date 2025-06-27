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
 * @date 28/08/2024 - 19:10
 */
public class RankCreateCommand extends BaseCommand {
    @Command(name = "rank.create", permission = "dojo.rank.create", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        boolean doPrefix;

        if (args.length < 2 ) {
            sender.sendMessage(CC.translate("&cUsage: &e/rank create (name) (doPrefix)"));
            sender.sendMessage(CC.translate("&7&oIf 'doPrefix' is true, the rank will automatically have a prefix."));
            return;
        }

        String name = args[0];
        Rank rank = Dojo.getInstance().getRankService().getRank(name);
        if (rank != null) {
            sender.sendMessage(CC.translate("&cA rank with that name already exists."));
            return;
        }

        switch (args[1]) {
            case "true":
                doPrefix = true;
                break;
            case "false":
                doPrefix = false;
                break;
            default:
                sender.sendMessage(CC.translate("&cInvalid boolean value. Please use 'true' or 'false'."));
                return;
        }

        Dojo.getInstance().getRankService().createRank(name, doPrefix);
        sender.sendMessage(CC.translate("&aSuccessfully created a new rank called &e" + name + "&a."));
    }
}
