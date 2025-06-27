package me.suff.dojo.tag.command.admin.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.tag.Tag;
import me.suff.dojo.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 13:53
 */
public class TagAdminDeleteCommand extends BaseCommand {
    @Command(name = "tagadmin.delete", permission = "dojo.ta.delete", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/tagadmin delete (name)"));
            return;
        }

        String name = args[0];
        Tag tag = Dojo.getInstance().getTagService().getTag(name);
        if (tag == null) {
            sender.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }

        Dojo.getInstance().getTagService().deleteTag(tag);
        sender.sendMessage(CC.translate("&aSuccessfully deleted the tag &e" + name + "&r&a."));
    }
}
