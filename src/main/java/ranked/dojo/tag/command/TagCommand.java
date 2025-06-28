package ranked.dojo.tag.command;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.tag.Tag;
import ranked.dojo.tag.TagService;
import ranked.dojo.tag.menu.TagMenu;
import ranked.dojo.util.CC;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

public class TagCommand extends BaseCommand {
    @Command(name = "tag")
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();
        TagService tagService = ranked.dojo.Dojo.getInstance().getTagService();

        if (args.length == 0) {
            sender.sendMessage(CC.translate(""));
            sender.sendMessage(CC.translate("&6&lTag Command Usage:"));
            sender.sendMessage(CC.translate(""));
            sender.sendMessage(CC.translate(" &e/tag create <name> <displayName> &7- &fCreate a new tag with a name and display name."));
            sender.sendMessage(CC.translate(" &e/tag delete <name> &7- &fDelete a tag by its name."));
            sender.sendMessage(CC.translate(" &e/tag info <name> &7- &fView detailed information about a tag."));
            sender.sendMessage(CC.translate(" &e/tag list &7- &fList all tags."));
            sender.sendMessage(CC.translate(" &e/tag setbold <name> <true/false> &7- &fSet whether a tag is bold."));
            sender.sendMessage(CC.translate(" &e/tag setcolor <name> <color> &7- &fSet the color of a tag (e.g. &9BLUE, &cRED, &eYELLOW)."));
            sender.sendMessage(CC.translate(" &e/tag setdisplayname <name> <displayName> &7- &fSet the display name of a tag."));
            sender.sendMessage(CC.translate(" &e/tag seticon <name> <material> &7- &fSet the icon of a tag (e.g. &aNAME_TAG, &aDIAMOND)."));
            sender.sendMessage(CC.translate(" &e/tag setitalic <name> <true/false> &7- &fSet whether a tag is italic."));
            sender.sendMessage(CC.translate(""));
            sender.sendMessage(CC.translate("&7Use &e/tags &7to open the tag menu."));
            sender.sendMessage(CC.translate(""));
            return;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "create":
                if (!sender.hasPermission("dojo.tag")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 3) {
                    sender.sendMessage(CC.translate("&cUsage: /tag create <name> <displayName>"));
                    return;
                }
                String name = args[1];
                String displayName = args[2];
                Tag tag = new Tag(name, displayName, Material.NAME_TAG, ChatColor.WHITE, 0, false, false);
                tagService.getTags().add(tag);
                tagService.saveTags();
                sender.sendMessage(CC.translate("&aTag created: &f" + name));
                break;
            case "delete":
                if (!sender.hasPermission("dojo.tag")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 2) {
                    sender.sendMessage(CC.translate("&cUsage: /tag delete <name>"));
                    return;
                }
                Tag toDelete = tagService.getTag(args[1]);
                if (toDelete == null) {
                    sender.sendMessage(CC.translate("&cTag not found."));
                    return;
                }
                tagService.getTags().remove(toDelete);
                tagService.saveTags();
                sender.sendMessage(CC.translate("&aTag deleted: &f" + args[1]));
                break;
            case "info":
                if (!sender.hasPermission("dojo.tag")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 2) {
                    sender.sendMessage(CC.translate("&cUsage: /tag info <name>"));
                    return;
                }
                Tag infoTag = tagService.getTag(args[1]);
                if (infoTag == null) {
                    sender.sendMessage(CC.translate("&cTag not found."));
                    return;
                }
                sender.sendMessage(CC.translate("&6Tag Info for &e" + infoTag.getName()));
                sender.sendMessage(CC.translate("&7Display Name: &f" + infoTag.getDisplayName()));
                ChatColor tagColor = infoTag.getColor();
                String colorDisplay = (tagColor != null ? tagColor + tagColor.name() : "None");
                sender.sendMessage(CC.translate("&7Color: &f" + colorDisplay));
                sender.sendMessage(CC.translate("&7Bold: &f" + infoTag.isBold()));
                sender.sendMessage(CC.translate("&7Italic: &f" + infoTag.isItalic()));
                sender.sendMessage(CC.translate("&7Icon: &f" + infoTag.getIcon()));
                break;
            case "list":
                if (!sender.hasPermission("dojo.tag")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                sender.sendMessage(CC.translate("&6Tags List:"));
                for (Tag t : tagService.getTags()) {
                    sender.sendMessage(CC.translate("&e- &f" + t.getName() + " &7(" + t.getDisplayName() + ")"));
                }
                break;
            case "setbold":
                if (!sender.hasPermission("dojo.tag")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 3) {
                    sender.sendMessage(CC.translate("&cUsage: /tag setbold <name> <true/false>"));
                    return;
                }
                Tag boldTag = tagService.getTag(args[1]);
                if (boldTag == null) {
                    sender.sendMessage(CC.translate("&cTag not found."));
                    return;
                }
                boolean bold = Boolean.parseBoolean(args[2]);
                boldTag.setBold(bold);
                tagService.saveTags();
                sender.sendMessage(CC.translate("&aSet bold for &f" + boldTag.getName() + "&a: &e" + bold));
                break;
            case "setcolor":
                if (!sender.hasPermission("dojo.tag")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 3) {
                    sender.sendMessage(CC.translate("&cUsage: /tag setcolor <name> <color>"));
                    return;
                }
                Tag colorTag = tagService.getTag(args[1]);
                if (colorTag == null) {
                    sender.sendMessage(CC.translate("&cTag not found."));
                    return;
                }
                try {
                    ChatColor color = ChatColor.valueOf(args[2].toUpperCase());
                    colorTag.setColor(color);
                    tagService.saveTags();
                    sender.sendMessage(CC.translate("&aSet color for &f" + colorTag.getName() + "&a: " + color + color.name()));
                } catch (Exception e) {
                    sender.sendMessage(CC.translate("&cInvalid color."));
                }
                break;
            case "setdisplayname":
                if (!sender.hasPermission("dojo.tag")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 3) {
                    sender.sendMessage(CC.translate("&cUsage: /tag setdisplayname <name> <displayName>"));
                    return;
                }
                Tag displayNameTag = tagService.getTag(args[1]);
                if (displayNameTag == null) {
                    sender.sendMessage(CC.translate("&cTag not found."));
                    return;
                }
                displayNameTag.setDisplayName(args[2]);
                tagService.saveTags();
                sender.sendMessage(CC.translate("&aSet display name for &f" + displayNameTag.getName() + "&a: &e" + args[2]));
                break;
            case "seticon":
                if (!sender.hasPermission("dojo.tag")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 3) {
                    sender.sendMessage(CC.translate("&cUsage: /tag seticon <name> <material>"));
                    return;
                }
                Tag iconTag = tagService.getTag(args[1]);
                if (iconTag == null) {
                    sender.sendMessage(CC.translate("&cTag not found."));
                    return;
                }
                try {
                    Material mat = Material.valueOf(args[2].toUpperCase());
                    iconTag.setIcon(mat);
                    tagService.saveTags();
                    sender.sendMessage(CC.translate("&aSet icon for &f" + iconTag.getName() + "&a: &e" + mat));
                } catch (Exception e) {
                    sender.sendMessage(CC.translate("&cInvalid material."));
                }
                break;
            case "setitalic":
                if (!sender.hasPermission("dojo.tag")) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                if (args.length < 3) {
                    sender.sendMessage(CC.translate("&cUsage: /tag setitalic <name> <true/false>"));
                    return;
                }
                Tag italicTag = tagService.getTag(args[1]);
                if (italicTag == null) {
                    sender.sendMessage(CC.translate("&cTag not found."));
                    return;
                }
                boolean italic = Boolean.parseBoolean(args[2]);
                italicTag.setItalic(italic);
                tagService.saveTags();
                sender.sendMessage(CC.translate("&aSet italic for &f" + italicTag.getName() + "&a: &e" + italic));
                break;
            default:
                sender.sendMessage(CC.translate("&cUnknown subcommand. Use /tag for help."));
        }
    }
    @Command(name = "tags")
    public void onTags(CommandArgs command) {
        if (!(command.getSender() instanceof org.bukkit.entity.Player)) {
            command.getSender().sendMessage(CC.translate("&cOnly players can use this command."));
            return;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) command.getSender();
        new TagMenu().openMenu(player);
    }
}