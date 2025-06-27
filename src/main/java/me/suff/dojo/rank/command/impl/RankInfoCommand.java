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
 * @date 28/08/2024 - 22:16
 */
public class RankInfoCommand extends BaseCommand {
    @Command(name = "rank.info", permission = "dojo.rank.info", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/rank info (name)"));
            return;
        }

        String name = args[0];
        Rank rank = Dojo.getInstance().getRankService().getRank(name);
        if (rank == null) {
            sender.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lDojo &8- &7Rank Information"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate(" &6&l● &fName: &e" + rank.getName()));
        sender.sendMessage(CC.translate(" &6&l● &fPrefix: &e" + rank.getPrefix()));
        sender.sendMessage(CC.translate(" &6&l● &fSuffix: &e" + rank.getSuffix()));
        sender.sendMessage(CC.translate(" &6&l● &fWeight: &e" + rank.getWeight()));
        sender.sendMessage(CC.translate(" &6&l● &fColor: &e" + rank.getColor() + rank.getColor().name()));
        sender.sendMessage(CC.translate(" &6&l● &fBold: &e" + rank.isBold()));
        sender.sendMessage(CC.translate(" &6&l● &fItalic: &e" + rank.isItalic()));
        sender.sendMessage(CC.translate(" &6&l● &fDefault: &e" + rank.isDefaultRank()));
        sender.sendMessage(CC.translate(" &6&l● &fPermissions:"));
        if (rank.getPermissions().isEmpty()) {
            sender.sendMessage(CC.translate("   &7None"));
            sender.sendMessage("");
            return;
        }

        rank.getPermissions().forEach(permission -> sender.sendMessage(CC.translate("   &f- &7" + permission)));
        sender.sendMessage("");
    }
}
