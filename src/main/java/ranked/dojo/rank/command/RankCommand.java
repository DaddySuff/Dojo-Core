package ranked.dojo.rank.command;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import org.bukkit.ChatColor;
import ranked.dojo.Dojo;
import ranked.dojo.namecolor.integration.RankIntegration;
import ranked.dojo.profile.Profile;
import ranked.dojo.rank.Rank;
import ranked.dojo.rank.RankService;
import ranked.dojo.rank.enums.RankCategory;
import ranked.dojo.util.BukkitUtils;
import ranked.dojo.util.CC;
import org.bukkit.command.CommandSender;


public class RankCommand extends BaseCommand {

    @Command(name = "rank", permission = "dojo.rank.help", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(CC.translate(""));
            sender.sendMessage(CC.translate("&6&lRank Command Usage:"));
            sender.sendMessage(CC.translate(""));
            sender.sendMessage(CC.translate(" &e/rank create <name> &7- &fCreate a new rank."));
            sender.sendMessage(CC.translate(" &e/rank delete <name> &7- &fDelete a rank by name."));
            sender.sendMessage(CC.translate(" &e/rank list &7- &fList all ranks."));
            sender.sendMessage(CC.translate(" &e/rank info <name> &7- &fView detailed information about a rank."));
            sender.sendMessage(CC.translate(" &e/rank setbold <name> <true/false> &7- &fSet whether a rank is bold."));
            sender.sendMessage(CC.translate(" &e/rank setcolor <name> <color> &7- &fSet the color of a rank (e.g. &9BLUE, &cRED, &eYELLOW)."));
            sender.sendMessage(CC.translate(" &e/rank setdefault <name> <true/false> &7- &fSet whether a rank is the default rank."));
            sender.sendMessage(CC.translate(" &e/rank setitalic <name> <true/false> &7- &fSet whether a rank is italic."));
            sender.sendMessage(CC.translate(" &e/rank setprefix <name> <prefix> &7- &fSet the prefix of a rank."));
            sender.sendMessage(CC.translate(" &e/rank setsuffix <name> <suffix> &7- &fSet the suffix of a rank."));
            sender.sendMessage(CC.translate(" &e/rank setweight <name> <weight> &7- &fSet the weight of a rank."));
            sender.sendMessage(CC.translate(" &e/rank setcategory <name> <enum> &7- &fSet the enum type of a rank (e.g. DEFAULT, STAFF, DONATOR, etc)."));
            sender.sendMessage(CC.translate(" &e/rank addpermission <name> <permission> &7- &fAdd a permission to a rank."));
            sender.sendMessage(CC.translate(" &e/rank help &7- &fShow detailed help for rank commands."));
            sender.sendMessage(CC.translate(""));
            return;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "create":
                if (!sender.hasPermission("dojo.rank.create")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 2) {
                    sender.sendMessage(CC.translate("&cUsage: /rank create <name>"));
                    return;
                }
                String name = args[1];
                RankService rankService = Dojo.getInstance().getRankService();
                if (rankService.getRanks().stream().anyMatch(r -> r.getName().equalsIgnoreCase(name))) {
                    sender.sendMessage(CC.translate("&cA rank with that name already exists."));
                    return;
                }
                Rank rank = new Rank();
                rank.setName(name);
                rank.setColor(ChatColor.WHITE);
                rank.setWeight(0);
                rank.setBold(false);
                rank.setItalic(false);
                rank.setDefaultRank(false);
                rank.setPrefix("");
                rank.setSuffix("");
                rank.setPermissions(new java.util.ArrayList<>());
                rankService.getRanks().add(rank);
                rankService.saveRanks();
                sender.sendMessage(CC.translate("&aRank created: &f" + name));
                break;
            case "delete":
                if (!sender.hasPermission("dojo.rank.delete")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 2) {
                    sender.sendMessage(CC.translate("&cUsage: /rank delete <name>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank toDelete = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (toDelete == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                rankService.getRanks().remove(toDelete);
                rankService.saveRanks();
                sender.sendMessage(CC.translate("&aRank deleted: &f" + args[1]));
                break;
            case "list":
                if (!sender.hasPermission("dojo.rank.list")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                sender.sendMessage(CC.translate("&6Ranks List:"));
                for (Rank r : rankService.getRanks()) {
                    sender.sendMessage(CC.translate("&e- &f" + r.getName() + " &7(" + r.getRankWithColor() + ")"));
                }
                break;
            case "info":
                if (!sender.hasPermission("dojo.rank.info")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 2) {
                    sender.sendMessage(CC.translate("&cUsage: /rank info <name>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank infoRank = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (infoRank == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                sender.sendMessage(CC.translate("&6Rank Info for &e" + infoRank.getName()));
                sender.sendMessage(CC.translate("&7Category: &f" + (infoRank.getRankCategory() != null ? infoRank.getRankCategory().name() : "None")));
                sender.sendMessage(CC.translate("&7Prefix: &f" + infoRank.getPrefix()));
                sender.sendMessage(CC.translate("&7Suffix: &f" + infoRank.getSuffix()));
                sender.sendMessage(CC.translate("&7Color: &f" + (infoRank.getColor() != null ? infoRank.getColor() + infoRank.getColor().name() : "None")));
                sender.sendMessage(CC.translate("&7Bold: &f" + infoRank.isBold()));
                sender.sendMessage(CC.translate("&7Italic: &f" + infoRank.isItalic()));
                sender.sendMessage(CC.translate("&7Weight: &f" + infoRank.getWeight()));
                sender.sendMessage(CC.translate("&7Permissions: &f" + (infoRank.getPermissions() != null ? String.join(", ", infoRank.getPermissions()) : "None")));
                break;
            case "setbold":
                if (!sender.hasPermission("dojo.rank.setbold")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(CC.translate("&cUsage: /rank setbold <name> <true/false>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank boldRank = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (boldRank == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                boolean bold = Boolean.parseBoolean(args[2]);
                boldRank.setBold(bold);
                rankService.saveRanks();
                sender.sendMessage(CC.translate("&aRank bold status updated: &f" + boldRank.getName() + " &7is now " + (bold ? "&lBold" : "&rNot Bold")));
                break;
            case "setcolor":
                if (!sender.hasPermission("dojo.rank.setcolor")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(CC.translate("&cUsage: /rank setcolor <name> <color>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank colorRank = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (colorRank == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                String colorName = args[2].toUpperCase();
                ChatColor color;
                try {
                    color = ChatColor.valueOf(colorName);
                } catch (Exception e) {
                    sender.sendMessage(CC.translate("&cInvalid color. Use a valid color name (e.g. &9BLUE, &cRED, &eYELLOW)."));
                    return;
                }
                colorRank.setColor(color);
                rankService.saveRanks();
                sender.sendMessage(CC.translate("&aRank color updated: &f" + colorRank.getName() + " &7is now " + color + colorName));
                // Refresh formatting for all online players with this rank
                BukkitUtils.getOnlinePlayers().forEach(p -> {
                    Profile profile = Dojo.getInstance().getProfileRepository().getProfile(p.getUniqueId());
                    if (profile != null && profile.getHighestRankBasedOnGrant() != null && profile.getHighestRankBasedOnGrant().getName().equalsIgnoreCase(colorRank.getName())) {
                        new RankIntegration(Dojo.getInstance()).refreshPlayerFormatting(p);
                    }
                });
                break;
            case "setdefault":
                if (!sender.hasPermission("dojo.rank.setdefault")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(CC.translate("&cUsage: /rank setdefault <name> <true/false>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank defaultRank = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (defaultRank == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                boolean isDefault = Boolean.parseBoolean(args[2]);
                defaultRank.setDefaultRank(isDefault);
                rankService.saveRanks();
                sender.sendMessage(CC.translate("&aRank default status updated: &f" + defaultRank.getName() + " &7is now " + (isDefault ? "&aDefault" : "&cNot Default")));
                break;
            case "setitalic":
                if (!sender.hasPermission("dojo.rank.setitalic")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(CC.translate("&cUsage: /rank setitalic <name> <true/false>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank italicRank = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (italicRank == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                boolean italic = Boolean.parseBoolean(args[2]);
                italicRank.setItalic(italic);
                rankService.saveRanks();
                sender.sendMessage(CC.translate("&aRank italic status updated: &f" + italicRank.getName() + " &7is now " + (italic ? "Italic" : "Not Italic")));
                break;
            case "setprefix":
                if (!sender.hasPermission("dojo.rank.setprefix")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(CC.translate("&cUsage: /rank setprefix <name> <prefix>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank prefixRank = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (prefixRank == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                String prefix = args[2];
                prefixRank.setPrefix(prefix);
                rankService.saveRanks();
                sender.sendMessage(CC.translate("&aRank prefix updated: &f" + prefixRank.getName() + " &7prefix set to " + prefix));
                break;
            case "setsuffix":
                if (!sender.hasPermission("dojo.rank.setsuffix")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(CC.translate("&cUsage: /rank setsuffix <name> <suffix>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank suffixRank = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (suffixRank == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                String suffix = args[2];
                suffixRank.setSuffix(suffix);
                rankService.saveRanks();
                sender.sendMessage(CC.translate("&aRank suffix updated: &f" + suffixRank.getName() + " &7suffix set to " + suffix));
                break;
            case "setweight":
                if (!sender.hasPermission("dojo.rank.setweight")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(CC.translate("&cUsage: /rank setweight <name> <weight>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank weightRank = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (weightRank == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                int weight;
                try {
                    weight = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(CC.translate("&cWeight must be a number."));
                    return;
                }
                weightRank.setWeight(weight);
                rankService.saveRanks();
                sender.sendMessage(CC.translate("&aRank weight updated: &f" + weightRank.getName() + " &7weight set to " + weight));
                break;
            case "addpermission":
                if (!sender.hasPermission("dojo.rank.addpermission")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(CC.translate("&cUsage: /rank addpermission <name> <permission>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank permissionRank = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (permissionRank == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                String permission = args[2];
                if (permissionRank.getPermissions().contains(permission)) {
                    sender.sendMessage(CC.translate("&cThis rank already has the specified permission."));
                    return;
                }
                permissionRank.getPermissions().add(permission);
                rankService.saveRanks();
                sender.sendMessage(CC.translate("&aPermission added to rank: &f" + permissionRank.getName() + " &7permission: " + permission));
                break;
            case "help":
                sender.sendMessage(CC.translate("&6&lRank Command Help:"));
                sender.sendMessage(CC.translate(" &e/rank create <name> &7- &fCreate a new rank."));
                sender.sendMessage(CC.translate(" &e/rank delete <name> &7- &fDelete a rank by name."));
                sender.sendMessage(CC.translate(" &e/rank list &7- &fList all ranks."));
                sender.sendMessage(CC.translate(" &e/rank info <name> &7- &fView detailed information about a rank."));
                sender.sendMessage(CC.translate(" &e/rank setbold <name> <true/false> &7- &fSet whether a rank is bold."));
                sender.sendMessage(CC.translate(" &e/rank setcolor <name> <color> &7- &fSet the color of a rank (e.g. &bBLUE, &cRED, &eYELLOW)."));
                sender.sendMessage(CC.translate(" &e/rank setdefault <name> <true/false> &7- &fSet whether a rank is the default rank."));
                sender.sendMessage(CC.translate(" &e/rank setitalic <name> <true/false> &7- &fSet whether a rank is italic."));
                sender.sendMessage(CC.translate(" &e/rank setprefix <name> <prefix> &7- &fSet the prefix of a rank."));
                sender.sendMessage(CC.translate(" &e/rank setsuffix <name> <suffix> &7- &fSet the suffix of a rank."));
                sender.sendMessage(CC.translate(" &e/rank setweight <name> <weight> &7- &fSet the weight of a rank."));
                sender.sendMessage(CC.translate(" &e/rank setcategory <name> <enum> &7- &fSet the enum type of a rank (e.g. DEFAULT, STAFF, DONATOR, etc)."));
                sender.sendMessage(CC.translate(" &e/rank addpermission <name> <permission> &7- &fAdd a permission to a rank."));
                sender.sendMessage(CC.translate(" &e/rank help &7- &fShow detailed help for rank commands."));
                break;
            case "setcategory":
                if (!sender.hasPermission("dojo.rank.setcategory")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(CC.translate("&cUsage: /rank setcategory <name> <category>"));
                    return;
                }
                rankService = Dojo.getInstance().getRankService();
                Rank categoryRank = rankService.getRanks().stream().filter(r -> r.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                if (categoryRank == null) {
                    sender.sendMessage(CC.translate("&cRank not found."));
                    return;
                }
                String categoryName = args[2].toUpperCase();
                try {
                    RankCategory category = RankCategory.valueOf(categoryName);
                    categoryRank.setRankCategory(category);
                    rankService.saveRanks();
                    sender.sendMessage(CC.translate("&aRank category updated: &f" + categoryRank.getName() + " &7is now &e" + category.name()));
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(CC.translate("&cInvalid category. Valid values: &f" + java.util.Arrays.toString(RankCategory.values())));
                }
                break;
            default:
                sender.sendMessage(CC.translate("&cUnknown subcommand. Use /rank for help."));
        }
    }
}
