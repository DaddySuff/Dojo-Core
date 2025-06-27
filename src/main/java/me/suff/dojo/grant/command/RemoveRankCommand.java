package me.suff.dojo.grant.command;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.grant.Grant;
import me.suff.dojo.profile.Profile;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 29/08/2024 - 13:42
 */
public class RemoveRankCommand extends BaseCommand {
    @Command(name = "removerank", permission = "dojo.mg.rr", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: &e/removerank (player) (rank)"));
            return;
        }

        String targetName = args[0];
        String rankName = args[1];

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        Profile profile = Dojo.getInstance().getProfileRepository().getProfileWithNoAdding(target.getUniqueId());
        if (profile == null) {
            sender.sendMessage(CC.translate("&cA profile with that name does not exist."));
            return;
        }

        if (profile.getGrants().isEmpty()) {
            sender.sendMessage(CC.translate("&cThat player does not have any grants."));
            return;
        }

        boolean updated = false;
        for (Grant grant : profile.getGrants()) {
            if (grant.getRank().getName().equalsIgnoreCase(rankName) && grant.isActive()) {
                grant.setActive(false);
                grant.setRemovedBy(sender.getName());
                grant.setRemovedAt(System.currentTimeMillis());
                updated = true;
                break;
            }
        }

        if (updated) {
            sender.sendMessage(CC.translate("&aSuccessfully removed the &e" + rankName + " &arank from &6" + target.getName() + "&a."));

            if (target.isOnline()) {
                target.getPlayer().sendMessage(CC.translate("&aYour &e" + rankName + " &arank has been removed by &6" + sender.getName() + "&a."));
            }
        } else {
            sender.sendMessage(CC.translate("&cThat player does not have the &e" + rankName + " &crank or the grant is not active."));
        }
    }
}