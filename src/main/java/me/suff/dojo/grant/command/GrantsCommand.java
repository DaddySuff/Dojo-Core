package me.suff.dojo.grant.command;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.grant.menu.grants.GrantsMenu;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 29/08/2024 - 15:43
 */
public class GrantsCommand extends BaseCommand {
    @Command(name = "grants", permission = "dojo.mg.grants")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/grants (player)"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        new GrantsMenu(target, false).openMenu(player);
    }
}