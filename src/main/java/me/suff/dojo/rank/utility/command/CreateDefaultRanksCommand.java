package me.suff.dojo.rank.utility.command;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.rank.utility.RankUtility;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 01/09/2024 - 13:42
 */
public class CreateDefaultRanksCommand extends BaseCommand {
    @Command(name = "createdefaultranks",permission = "dojo.cdranks", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        if (sender instanceof Player) {
            sender.sendMessage(CC.translate("&cThis command can only be executed from the console."));
            return;
        }

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            sender.sendMessage(CC.translate("&cYou can only create default ranks when no players are online."));
            return;
        }

        RankUtility.createDefaultRanks();
    }
}