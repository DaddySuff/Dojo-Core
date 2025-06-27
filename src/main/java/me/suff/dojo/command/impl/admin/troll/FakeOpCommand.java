package me.suff.dojo.command.impl.admin.troll;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 14:31
 */
public class FakeOpCommand extends BaseCommand {
    @Override
    @Command(name = "fakeop", permission = "flower.command.fakeop", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        String senderName;
        if (sender instanceof Player) {
            senderName = sender.getName();
        } else {
            senderName = "Server";
        }

        targetPlayer.sendMessage(CC.translate("&7&o[" + senderName + "&7&o: Opped " + targetPlayer.getName() + "&7&o]"));

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.sendMessage(CC.translate("&7&o[" + senderName + "&7&o: Opped " + targetPlayer.getName() + "&7&o]"));
        }
    }
}