package me.suff.dojo.tag.command.admin.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.tag.Tag;
import me.suff.dojo.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 14:02
 */
public class TagAdminSetIconCommand extends BaseCommand {
    @Command(name = "tagadmin.seticon", permission = "dojo.ta.seticon")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/tagadmin seticon (name)"));
            return;
        }

        String name = args[0];
        Tag tag = Dojo.getInstance().getTagService().getTag(name);
        if (tag == null) {
            player.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }

        tag.setIcon(player.getItemInHand().getType());
        tag.setDurability(player.getItemInHand().getDurability());
        Dojo.getInstance().getTagService().saveTag(tag);
        player.sendMessage(CC.translate("&aSuccessfully set the icon of the tag &e" + name + "&r&a to &6" + player.getItemInHand().getType().name() + "&r&a."));
    }
}
