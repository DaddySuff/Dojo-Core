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
 * @date 03/09/2024 - 13:56
 */
public class TagAdminInfoCommand extends BaseCommand {
    @Command(name = "tagadmin.info", permission = "dojo.ta.info", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/tagadmin info (name)"));
            return;
        }

        String name = args[0];
        Tag tag = Dojo.getInstance().getTagService().getTag(name);
        if (tag == null) {
            sender.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lDojo &8- &7Tag Information"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate(" &6&l● &fName: &e" + tag.getName()));
        sender.sendMessage(CC.translate(" &6&l● &fDisplay Name: &e" + tag.getDisplayName()));
        sender.sendMessage(CC.translate(" &6&l● &fIcon: &e" + tag.getIcon().name()));
        sender.sendMessage(CC.translate(" &6&l● &fColor: &e" + tag.getColor().name()));
        sender.sendMessage(CC.translate(" &6&l● &fBold: &e" + tag.isBold()));
        sender.sendMessage(CC.translate(" &6&l● &fItalic: &e" + tag.isItalic()));
        sender.sendMessage("");
        sender.sendMessage(CC.translate(" &f&l● &7Appearance: &r" + tag.getNiceName()));
        sender.sendMessage("");
    }
}
