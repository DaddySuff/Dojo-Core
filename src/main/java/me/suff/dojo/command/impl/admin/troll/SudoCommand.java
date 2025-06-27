package me.suff.dojo.command.impl.admin.troll;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudoCommand extends BaseCommand {

    @Command(name = "sudo", permission = "artex.sudo", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();

        if (args.length() < 2) {
            sender.sendMessage(CC.translate("&cUsage: /sudo &e<player> <command/message>"));
            return;
        }

        Player target = Bukkit.getPlayer(args.getArgs(0));

        if (target == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        String commandOrMessage = args.getArgs(1);
        for (int i = 2; i < args.length(); i++) {
            commandOrMessage += " " + args.getArgs(i);
        }

        if (commandOrMessage.startsWith("/")) {
            // Execute as command
            target.performCommand(commandOrMessage.substring(1));
            sender.sendMessage(CC.translate("&aForced &e" + target.getName() + " &ato run: &6" + commandOrMessage));
        } else {
            // Send as chat message
            target.chat(commandOrMessage);
            sender.sendMessage(CC.translate("&aForced &e" + target.getName() + " &ato say: &6" + commandOrMessage));
        }
    }
}
