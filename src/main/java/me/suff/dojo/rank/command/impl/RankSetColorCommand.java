package me.suff.dojo.rank.command.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.rank.Rank;
import me.suff.dojo.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 30/08/2024 - 22:46
 */
public class RankSetColorCommand extends BaseCommand {
    @Command(name = "rank.setcolor", permission = "dojo.rank.setcolor", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: &e/rank setcolor (name) (color)"));
            return;
        }

        String name = args[0];
        Rank rank = Dojo.getInstance().getRankService().getRank(name);
        if (rank == null) {
            sender.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        ChatColor color;

        try {
            color = ChatColor.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CC.translate("&cInvalid color. Please use a valid ChatColor. &7&o(Example: GOLD)"));
            return;
        }

        rank.setColor(color);
        Dojo.getInstance().getRankService().saveRank(rank);
        sender.sendMessage(CC.translate("&aSuccessfully set the color of &e" + name + " &ato &6" + color + "&a."));
    }
}
