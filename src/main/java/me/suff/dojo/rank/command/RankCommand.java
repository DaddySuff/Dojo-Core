package me.suff.dojo.rank.command;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 28/08/2024 - 19:06
 */
public class RankCommand extends BaseCommand {
    @Command(name = "rank", permission = "dojo.rank.help", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        int page = 1;

        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage("");
                sender.sendMessage(CC.translate("&6&lDojo &8- &7Rank Commands - &7(1&8/&73)"));
                sender.sendMessage("");
                sender.sendMessage(CC.translate(" &6&l● &f/rank create &e(name) - &7Create a rank"));
                sender.sendMessage(CC.translate(" &6&l● &f/rank delete &e(name) - &7Delete a rank"));
                sender.sendMessage(CC.translate(" &6&l● &f/rank list - &7List all ranks"));
                sender.sendMessage(CC.translate(" &6&l● &f/rank info &e(name) - &7View rank information"));
                sender.sendMessage("");
                sender.sendMessage(CC.translate("&7&oFor more detailed instructions, use &6&o/rank help&7&o."));
                sender.sendMessage("");
                return;
            }
        }

        if (page < 1 || page > 3) {
            sender.sendMessage("");
            sender.sendMessage(CC.translate("&6&lDojo &8- &7Rank Commands - &7(1&8/&73)"));
            sender.sendMessage("");
            sender.sendMessage(CC.translate(" &6&l● &f/rank create &e(name) - &7Create a rank"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank delete &e(name) - &7Delete a rank"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank list - &7List all ranks"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank info &e(name) - &7View rank information"));
            sender.sendMessage("");
            sender.sendMessage(CC.translate("&7&oFor more detailed instructions, use &6&o/rank help&7&o."));
            sender.sendMessage("");
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lArtex &8- &7Rank Commands - &7(" + page + "&8/&73)"));

        if (page == 1) {
            sender.sendMessage("");
            sender.sendMessage(CC.translate(" &6&l● &f/rank create &e(name) - &7Create a rank"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank delete &e(name) - &7Delete a rank"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank list - &7List all ranks"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank info &e(name) - &7View rank information"));
            sender.sendMessage("");
            sender.sendMessage(CC.translate("&7&oFor more detailed instructions, use &6&o/rank help&7&o."));
            sender.sendMessage("");
        } else if (page == 2) {
            sender.sendMessage("");
            sender.sendMessage(CC.translate(" &6&l● &f/rank setprefix &e(name) (prefix) - &7Set the prefix of a rank"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank setsuffix &e(name) (suffix) - &7Set the suffix of a rank"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank setcolor &e(name) (color) - &7Set the color of a rank"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank setbold &e(name) (true/false) - &7Makes the rank bold"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank setitalic &e(name) (true/false) - &7Makes the rank italic"));
        } else {
            sender.sendMessage("");
            sender.sendMessage(CC.translate(" &6&l● &f/rank setweight &e(name) (weight) - &7Set the weight of a rank"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank setdefault &e(name) - &7Set the default rank"));
            sender.sendMessage(CC.translate(" &6&l● &f/rank addpermission &e(name) (permission) - &7Add a permission to a rank"));
        }

        sender.sendMessage("");
    }
}