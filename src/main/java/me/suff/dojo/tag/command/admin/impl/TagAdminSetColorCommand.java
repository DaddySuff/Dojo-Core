package me.suff.dojo.tag.command.admin.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.tag.Tag;
import me.suff.dojo.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 14:06
 */
public class TagAdminSetColorCommand extends BaseCommand {
    @Command(name = "tagadmin.setcolor", permission = "dojo.ta.setcolor", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: &e/tagadmin setcolor (name) (color)"));
            return;
        }

        String name = args[0];
        Tag tag = Dojo.getInstance().getTagService().getTag(name);
        if (tag == null) {
            sender.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }
        ChatColor color;

        try {
            color = ChatColor.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CC.translate("&cInvalid color."));
            return;
        }

        tag.setColor(color);
        Dojo.getInstance().getTagService().saveTag(tag);
        sender.sendMessage(CC.translate("&aSuccessfully set the color of the tag &e" + name + "&r&a to &6" + color + "&r&a."));
    }
}
