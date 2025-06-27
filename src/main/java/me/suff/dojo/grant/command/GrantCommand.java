package me.suff.dojo.grant.command;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.grant.menu.grant.GrantMenu;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Artex
 * @date 29/08/2024 - 09:34
 */
public class GrantCommand extends BaseCommand {
    @Command(name = "grant", permission = "admin.mg.grant")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/grant (player) &7[reason]"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        String reason;
        if (args.length > 2) {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            new GrantMenu(target, reason).openMenu(player);
            return;
        }

        reason = Dojo.getInstance().getConfig().getString("grant.default-reason");
        new GrantMenu(target, reason).openMenu(player);
    }
}