package me.suff.dojo.chat.command;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 04/06/2024 - 20:28
 */
public class MuteChatCommand extends BaseCommand {
    @Command(name = "mutechat", permission = "dojo.command.mutechat", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Dojo.getInstance().getChatService().setChatMuted(true);
        Bukkit.broadcastMessage(CC.translate("&c&lChat has been muted by " + sender.getName() + "."));
    }
}