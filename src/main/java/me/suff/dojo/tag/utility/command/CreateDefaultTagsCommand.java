package me.suff.dojo.tag.utility.command;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.tag.utility.TagUtility;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @since 08/02/2025
 */
public class CreateDefaultTagsCommand extends BaseCommand {
    @Command(name = "createdefaulttags", permission = "dojo.cdtags", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            sender.sendMessage(CC.translate("&cYou can only create default tags when no players are online."));
            return;
        }

        TagUtility.createDefaultTags();
    }
}