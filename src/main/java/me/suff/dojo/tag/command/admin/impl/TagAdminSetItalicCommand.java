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
 * @date 03/09/2024 - 14:11
 */
public class TagAdminSetItalicCommand extends BaseCommand {
    @Command(name = "tagadmin.setitalic", permission = "dojo.ta.setitalic", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: &e/tagadmin setitalic (name) (true/false)"));
            return;
        }

        String name = args[0];
        Tag tag = Dojo.getInstance().getTagService().getTag(name);
        if (tag == null) {
            sender.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }

        boolean italic;

        try {
            italic = Boolean.parseBoolean(args[1]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CC.translate("&cIncorrect usage. Please use 'true' or 'false'."));
            return;
        }

        tag.setItalic(italic);
        Dojo.getInstance().getTagService().saveTag(tag);
        sender.sendMessage(CC.translate("&aSuccessfully set the italic of the tag &e" + name + "&r&a to &6" + italic + "&r&a."));
    }
}
