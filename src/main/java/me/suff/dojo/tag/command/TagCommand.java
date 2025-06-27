package me.suff.dojo.tag.command;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.tag.menu.TagMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 01/09/2024 - 20:28
 */
public class TagCommand extends BaseCommand {
    @Command(name = "tag", aliases = {"tags"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new TagMenu().openMenu(player);
    }
}