package ranked.dojo.command.impl.staff;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ranked.dojo.Dojo;
import ranked.dojo.rank.Rank;
import ranked.dojo.util.CC;
import ranked.dojo.punishment.Punishment;
import ranked.dojo.punishment.PunishmentHandler;
import ranked.dojo.punishment.enums.EnumPunishmentType;
import ranked.dojo.profile.Profile;
import ranked.dojo.rank.enums.RankCategory;

import java.util.Arrays;

/**
 * @author curxxed
 * @date 6/28/2025
 * @project Dojo
 */
public class BanCommand extends BaseCommand {
    @Override
    @Command(name = "ban", permission = "dojo.command.ban", aliases = {"ban"}, description = "Ban a player from the network")
    public void onCommand(CommandArgs commandArgs) {
        String[] argsArray = commandArgs.getArgs();
        if (argsArray.length < 2 || (argsArray[0].equalsIgnoreCase("confirm") && argsArray.length < 3)) {
            commandArgs.getSender().sendMessage(CC.translate(CC.PREFIX + "&cUsage: /ban <player> <duration> [reason] [-s]"));
            return;
        }

        boolean isConfirm = argsArray[0].equalsIgnoreCase("confirm");
        int base = isConfirm ? 1 : 0;
        String targetName = argsArray[base];
        String durationArg = argsArray[base + 1];
        boolean silent = argsArray[argsArray.length - 1].equalsIgnoreCase("-s");
        String reason;
        if (silent) {
            reason = argsArray.length > (base + 2) ? String.join(" ", Arrays.copyOfRange(argsArray, base + 2, argsArray.length - 1)) : "No reason specified";
        } else {
            reason = argsArray.length > (base + 2) ? String.join(" ", Arrays.copyOfRange(argsArray, base + 2, argsArray.length)) : "No reason specified";
        }

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            commandArgs.getSender().sendMessage(CC.translate(CC.PREFIX + "&cPlayer not found."));
            return;
        }
        long durationMillis = parseDuration(durationArg);
        if (durationMillis == 0 && !durationArg.equalsIgnoreCase("perm") && !durationArg.equalsIgnoreCase("permanent")) {
            commandArgs.getSender().sendMessage(CC.translate(CC.PREFIX + "&cInvalid duration. Use m/h/d/perm."));
            return;
        }
        
        Punishment punishment = new Punishment();
        punishment.setPunishmentType(EnumPunishmentType.BAN);
        punishment.setPunisher(commandArgs.getSender().getName());
        punishment.setPunishReason(reason);
        punishment.setDuration(durationMillis);
        punishment.setPermanent(durationMillis == -1L);
        punishment.setAddedAt(System.currentTimeMillis());
        punishment.setExpiration(durationMillis == -1L ? -1L : System.currentTimeMillis() + durationMillis);
        punishment.setActive(true);
        Profile targetProfile = Dojo.getInstance().getProfileRepository().getProfile(target.getUniqueId());
        boolean isStaff = targetProfile.getHighestRankBasedOnGrant().getRankCategory() == RankCategory.STAFF;
        if (isStaff && !isConfirm) {
            commandArgs.getSender().sendMessage(CC.translate(CC.PREFIX + "&cYou are about to ban a STAFF member! To confirm, use: &e/ban confirm " + targetName + " " + durationArg + (reason.equals("No reason specified") ? "" : " " + reason) + (silent ? " -s" : "")));
            return;
        }
        //TODO: Only make /ban confirm valid if you already tried banning a Staff Member
        if (isConfirm) {
            if (commandArgs.getArgs().length < 3) {
                commandArgs.getSender().sendMessage(CC.translate(CC.PREFIX + "&cUsage: /ban confirm <player> <duration> [reason] [-s]"));
                return;
            }
            targetName = commandArgs.getArgs()[1];
            durationArg = commandArgs.getArgs()[2];
            target = Bukkit.getPlayerExact(targetName);
            if (target == null) {
                commandArgs.getSender().sendMessage(CC.translate(CC.PREFIX + "&cPlayer not found."));
                return;
            }
            argsArray = commandArgs.getArgs();
            silent = argsArray[argsArray.length - 1].equalsIgnoreCase("-s");
            if (silent) {
                reason = argsArray.length > 4 ? String.join(" ", Arrays.copyOfRange(argsArray, 3, argsArray.length - 1)) : "No reason specified";
            } else {
                reason = argsArray.length > 3 ? String.join(" ", Arrays.copyOfRange(argsArray, 3, argsArray.length)) : "No reason specified";
            }
        }
        PunishmentHandler.banPlayer(target.getUniqueId(), durationMillis);
        String banMsg = "\n&8&m--------------------------------------\n" +
                "&c&lYou have been banned!" + "\n\n" +
                "&6Reason: &f" + reason + "\n" +
                "&6Duration: &f" + (durationMillis == -1L ? "Permanent" : formatDuration(durationMillis)) + "\n" +
                "\n&7If you believe this is a mistake, contact staff." +
                "\n&8&m--------------------------------------\n";
        target.kickPlayer(CC.translate(banMsg));
        String senderName = commandArgs.getSender() instanceof Player ? ((Player) commandArgs.getSender()).getName() : "&fCONSOLE";
        String senderColor = "";
        if (commandArgs.getSender() instanceof Player) {
            Profile senderProfile = Dojo.getInstance().getProfileRepository().getProfile(((Player) commandArgs.getSender()).getUniqueId());
            Rank senderRank = senderProfile != null ? senderProfile.getHighestRankBasedOnGrant() : null;
            if (senderRank != null && senderRank.getColor() != null) {
                senderColor = senderRank.getColor().toString();
            }
        }
        Rank targetRank = targetProfile != null ? targetProfile.getHighestRankBasedOnGrant() : null;
        String targetColor = (targetRank != null && targetRank.getColor() != null) ? targetRank.getColor().toString() : "";
        String banBroadcast = CC.translate((silent ? "&7(Silent) " : "") + targetColor + target.getName() + "&a has been banned by " + senderColor + senderName + "&a for: &r" + reason);
        if (silent) {
            for (Player p : target.getServer().getOnlinePlayers()) {
                Profile profile = Dojo.getInstance().getProfileRepository().getProfile(p.getUniqueId());
                boolean isStaffCategory = profile != null && profile.getHighestRankBasedOnGrant().getRankCategory() == RankCategory.STAFF;
                if (isStaffCategory || p.isOp()) {
                    p.sendMessage(banBroadcast);
                }
            }
        } else {
            for (Player p : target.getServer().getOnlinePlayers()) {
                p.sendMessage(banBroadcast);
            }
        }
        commandArgs.getSender().sendMessage(CC.translate(CC.PREFIX + "&aBanned &e" + target.getName() + " &afor &e" + (durationMillis == -1L ? "Permanent" : formatDuration(durationMillis)) + "&a."));
    }

    private long parseDuration(String arg) {
        if (arg.equalsIgnoreCase("perm") || arg.equalsIgnoreCase("permanent")) return -1L;
        try {
            char unit = arg.charAt(arg.length() - 1);
            long value = Long.parseLong(arg.substring(0, arg.length() - 1));
            switch (unit) {
                case 'm': return value * 60_000L;
                case 'h': return value * 60 * 60_000L;
                case 'd': return value * 24 * 60 * 60_000L;
                default: return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private String formatDuration(long millis) {
        if (millis == -1L) return "Permanent";
        long seconds = millis / 1000;
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m");
        return sb.toString().trim();
    }
}
