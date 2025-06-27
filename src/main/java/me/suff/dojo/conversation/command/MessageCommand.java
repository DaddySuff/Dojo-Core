package me.suff.dojo.conversation.command;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Artex
 * @date 11/09/2024 - 23:03
 */
public class MessageCommand extends BaseCommand {
    @Command(name = "message", aliases = {"msg", "tell", "whisper"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: &e/message &7<player> <message>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        if (target == player) {
            player.sendMessage(CC.translate("&cYou can't message yourself."));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Dojo.getInstance().getConversationHandler().startConversation(player.getUniqueId(), target.getUniqueId(), message);
    }
}