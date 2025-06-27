package me.suff.dojo.tag.command.admin.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.tag.Tag;
import me.suff.dojo.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Artex
 * @date 01/09/2024 - 20:31
 */
public class TagAdminCreateCommand extends BaseCommand {
    @Command(name = "tagadmin.create", permission = "dojo.ta.create", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage("");
            sender.sendMessage(CC.translate("&cUsage: &e/tagadmin create (name) (displayName)"));
            sender.sendMessage("");
            sender.sendMessage(CC.translate(
                    "&cNote: &cPlease avoid using color codes in the display name. \n&cUse &e'/tagadmin setcolor' &cfor color settings. \n&cBold and italic may not work \n&cwith additional formatting codes like &nunderline&r&7."
            ));
            sender.sendMessage("");
            return;

        }

        String name = args[0];
        String displayName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Tag tag = Dojo.getInstance().getTagService().getTag(name);
        if (tag != null) {
            sender.sendMessage(CC.translate("&cA tag with that name already exists."));
            return;
        }

        Dojo.getInstance().getTagService().createTag(name, displayName, Material.NAME_TAG, ChatColor.WHITE, 0, false, false);
        sender.sendMessage(CC.translate("&aSuccessfully created a new tag called &e" + name + " &awith the display name &f" + displayName + "&r&a."));
    }
}
