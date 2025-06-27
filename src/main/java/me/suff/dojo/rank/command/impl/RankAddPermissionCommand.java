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
 * @date 30/08/2024 - 22:55
 */
public class RankAddPermissionCommand extends BaseCommand {
    @Command(name = "rank.addpermission", permission = "dojo.rank.addpermission", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: &e/rank addpermission (name) (permission)"));
            return;
        }

        String name = args[0];
        Rank rank = Dojo.getInstance().getRankService().getRank(name);
        if (rank == null) {
            sender.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        String permission = args[1];

        if (rank.getPermissions().contains(permission)) {
            rank.getPermissions().remove(permission);
            Dojo.getInstance().getRankService().saveRank(rank);
            sender.sendMessage(CC.translate("&aSuccessfully &cremoved &athe permission &e" + permission + " &afrom &6" + name + "&a."));
            return;
        }

        rank.getPermissions().add(permission);
        Dojo.getInstance().getRankService().saveRank(rank);
        sender.sendMessage(CC.translate("&aSuccessfully &eadded the permission &e" + permission + " &ato &6" + name + "&a."));
    }
}
