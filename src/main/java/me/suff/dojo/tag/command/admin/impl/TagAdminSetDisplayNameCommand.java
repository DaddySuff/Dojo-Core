package me.suff.dojo.tag.command.admin.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.tag.Tag;
import me.suff.dojo.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 14:03
 */
public class TagAdminSetDisplayNameCommand extends BaseCommand {
    @Command(name = "tagadmin.setdisplayname", aliases = {"tagadmin.setdisplay"},permission = "dojo.ta.setdisplayname", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: &e/tagadmin setdisplayname (name) (displayName)"));
            return;
        }

        String name = args[0];
        String displayName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Tag tag = Dojo.getInstance().getTagService().getTag(name);
        if (tag == null) {
            sender.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }

        tag.setDisplayName(displayName);
        Dojo.getInstance().getTagService().saveTag(tag);
        sender.sendMessage(CC.translate("&aSuccessfully set the display name of the tag &e" + name + "&r&a to &6" + displayName + "&r&a."));
    }
}
