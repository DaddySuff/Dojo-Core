package me.suff.dojo.grant.command;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.grant.Grant;
import me.suff.dojo.locale.Locale;
import me.suff.dojo.profile.Profile;
import me.suff.dojo.rank.Rank;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Artex
 * @date 28/08/2024 - 22:44
 */
public class SetRankCommand extends BaseCommand {
    @Command(name = "setrank", permission = "dojo.mg.rankset", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 4) {
            sender.sendMessage(CC.translate("&cUsage: &e/setrank (name) (rank) (duration/perm) &7(reason)"));
            sender.sendMessage(CC.translate("&cExample: &e/setrank &ASUUFF &4Owner &cperm &7Reason can have spaces."));
            return;
        }

        String targetName = args[0];
        String rankName = args[1];
        long duration;
        String reason = String.join(" ", Arrays.copyOfRange(args, 3, args.length));

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        Profile profile = Dojo.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        Rank grantedRank = Dojo.getInstance().getRankService().getRank(rankName);

        if (grantedRank == null) {
            sender.sendMessage(CC.translate("&cThe rank &e'" + rankName + "' &cdoes not exist."));
            return;
        }

        boolean isPermanent;
        if (args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permanent")) {
            duration = -1;
            isPermanent = true;
        } else {
            try {
                duration = Long.parseLong(args[2]);
                isPermanent = false;
            } catch (NumberFormatException e) {
                sender.sendMessage(CC.translate("&cInvalid duration number!"));
                return;
            }
        }

        if (profile.getGrants().stream().anyMatch(grant1 -> grant1.getRank().equals(grantedRank) && grant1.isActive())) {
            sender.sendMessage(CC.translate("&cThat player already has the rank &e'" + rankName + "' &cgranted and active."));
            return;
        }

        Grant grant = new Grant();
        grant.setRank(grantedRank.getName());
        grant.setDuration(duration);
        grant.setReason(reason);
        grant.setAddedBy(sender.getName());
        grant.setAddedAt(System.currentTimeMillis());
        grant.setPermanent(isPermanent);
        grant.setActive(true);
        grant.setAddedOn(Locale.SERVER_NAME.getString());

        profile.getGrants().add(grant);
        profile.setRank(grantedRank);

        sender.sendMessage(CC.translate("&aYou have granted the rank &e" + rankName + " &ato &6" + targetName + "&a."));

        if (target.isOnline()) {
            target.getPlayer().sendMessage(CC.translate("&aYou have been granted the rank &e" + rankName + "&a."));
        }
    }
}