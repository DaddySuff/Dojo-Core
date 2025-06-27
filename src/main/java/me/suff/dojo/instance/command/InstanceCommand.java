package me.suff.dojo.instance.command;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.instance.menu.InstanceMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 30/08/2024 - 21:00
 */
public class InstanceCommand extends BaseCommand {
    @Command(name = "instance", permission = "dojo.instance")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new InstanceMenu().openMenu(player);
    }
}
