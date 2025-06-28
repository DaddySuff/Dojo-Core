package ranked.dojo.command.impl.staff;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import org.bukkit.entity.Player;
import ranked.dojo.Dojo;
import ranked.dojo.profile.Profile;
import ranked.dojo.profile.ProfileRepository;
import ranked.dojo.rank.Rank;
import ranked.dojo.rank.enums.RankCategory;
import ranked.dojo.util.CC;

/**
 * @author curxxed
 * @date 6/28/2025
 * @project Dojo
 */
public class KickCommand extends BaseCommand {
    @Override
    @Command(name = "kick", permission = "dojo.command.kick", inGameOnly = false)
    public void onCommand(CommandArgs args) {
        if (args.length() < 2) {
            args.getSender().sendMessage(CC.translate("&cUsage: /kick <player> <reason> [-s]"));
            return;
        }

        String targetName = args.getArgs(0);
        String[] argsArray = args.getArgs();
        boolean silent = false;
        String reason;
        if (argsArray[argsArray.length - 1].equalsIgnoreCase("-s")) {
            silent = true;
            reason = String.join(" ", java.util.Arrays.copyOfRange(argsArray, 1, argsArray.length - 1));
        } else {
            reason = String.join(" ", java.util.Arrays.copyOfRange(argsArray, 1, argsArray.length));
        }
        Player target = args.getSender().getServer().getPlayerExact(targetName);

        if (target == null) {
            args.getSender().sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        String kickerName = args.getSender() instanceof Player ? ((Player) args.getSender()).getName() : "&fCONSOLE";
        String kickerColor = "";
        if (args.getSender() instanceof Player) {
            Profile kickerProfile = Dojo.getInstance().getProfileRepository().getProfile(((Player) args.getSender()).getUniqueId());
            Rank kickerRank = kickerProfile != null ? kickerProfile.getHighestRankBasedOnGrant() : null;
            if (kickerRank != null && kickerRank.getColor() != null) {
                kickerColor = kickerRank.getColor().toString();
            }
        }
        String kickMessage = CC.translate("&c&lYou have been kicked!\n" +
                "&7By: " + kickerColor + kickerName + "&r\n" +
                "&7Reason: &e" + reason);

        target.kickPlayer(kickMessage);

        Profile targetProfile = Dojo.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        Rank targetRank = targetProfile != null ? targetProfile.getHighestRankBasedOnGrant() : null;
        String targetColor = (targetRank != null && targetRank.getColor() != null) ? targetRank.getColor().toString() : "";
        String broadcastMsg = CC.translate("&a" + targetColor + target.getName() + "&r has been kicked by " + kickerColor + kickerName + ". Reason: &r" + reason);
        if (silent) {
            ProfileRepository profileRepo = Dojo.getInstance().getProfileRepository();
            for (Player p : target.getServer().getOnlinePlayers()) {
                Profile profile = profileRepo.getProfile(p.getUniqueId());
                Rank rank = profile != null ? profile.getHighestRankBasedOnGrant() : null;
                boolean isStaffCategory = rank != null && rank.getRankCategory() == RankCategory.STAFF;
                if (isStaffCategory || p.isOp()) {
                    p.sendMessage(CC.translate("&7&l(Silent) &r" + broadcastMsg));
                }
            }
        } else {
            for (Player p : target.getServer().getOnlinePlayers()) {
                p.sendMessage(broadcastMsg);
            }
        }

        args.getSender().sendMessage(CC.translate("&aYou kicked &e" + target.getName() + " &afor: &e" + reason));
    }
}
