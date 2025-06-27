package me.suff.dojo.command.impl.user;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.BungeeUtil;
import me.suff.dojo.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 15/09/2024 - 18:11
 */
public class JoinCommand extends BaseCommand {
    @Command(name = "join")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length <1) {
            player.sendMessage(CC.translate("&cUsage: &e/join <server>"));
            return;
        }

        BungeeUtil.sendPlayer(player, args[0]);
        player.sendMessage(CC.translate("&aSending you to &e" + args[0] + "&a..."));
    }
}