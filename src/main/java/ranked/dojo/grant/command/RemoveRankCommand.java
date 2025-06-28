package ranked.dojo.grant.command;

import ranked.dojo.Dojo;
import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.grant.Grant;
import ranked.dojo.namecolor.integration.RankIntegration;
import ranked.dojo.profile.Profile;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;


public class RemoveRankCommand extends BaseCommand {
    @Command(name = "removerank", permission = "dojo.command.removerank", inGameOnly = false)
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
                new RankIntegration(Dojo.getInstance()).refreshPlayerFormatting(target.getPlayer());
                target.getPlayer().sendMessage(CC.translate("&aYour &e" + rankName + " &arank has been removed by &6" + sender.getName() + "&a."));
            }
        } else {
            sender.sendMessage(CC.translate("&cThat player does not have the &e" + rankName + " &crank or the grant is not active."));
        }
    }
}