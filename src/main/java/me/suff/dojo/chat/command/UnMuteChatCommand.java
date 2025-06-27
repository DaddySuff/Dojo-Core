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
 * @date 04/06/2024 - 20:41
 */
public class UnMuteChatCommand extends BaseCommand {
    @Command(name = "unmutechat", permission = "dojo.command.unmutechat", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Dojo.getInstance().getChatService().setChatMuted(false);
        Bukkit.broadcastMessage(CC.translate("&a&lChat has been unmuted by " + sender.getName() + "."));
    }
}