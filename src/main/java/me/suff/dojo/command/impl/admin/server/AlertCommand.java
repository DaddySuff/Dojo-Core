package me.suff.dojo.command.impl.admin.server;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Artex
 * @date 15/11/2024 - 10:41
 */
public class AlertCommand extends BaseCommand {
    @Command(name = "alert", permission = "artex.command.alert", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/alert <message>"));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(CC.translate("&4&lAlert &8- &e" + message)));
    }
}