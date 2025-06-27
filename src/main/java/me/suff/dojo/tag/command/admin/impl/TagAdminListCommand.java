package me.suff.dojo.tag.command.admin.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.tag.TagService;
import me.suff.dojo.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 13:58
 */
public class TagAdminListCommand extends BaseCommand {
    @Command(name = "tagadmin.list", permission = "dojo.ta.list", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        TagService tagService = Dojo.getInstance().getTagService();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lDojo &8- &7Tag List"));
        sender.sendMessage("");

        if (tagService.getTags().isEmpty()) {
            sender.sendMessage(CC.translate("&cNo tags found."));
            sender.sendMessage("");
            return;
        }

        tagService.getTags().stream().sorted().forEach(tag -> sender.sendMessage(CC.translate("&6&l● &e" + tag.getName() + " &f- &7" + tag.getNiceName())));

        sender.sendMessage("");
    }
}
