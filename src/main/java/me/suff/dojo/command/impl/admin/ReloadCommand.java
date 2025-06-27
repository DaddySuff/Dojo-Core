package me.suff.dojo.command.impl.admin;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import me.suff.dojo.util.ProjectInfo;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 15:55
 */
public class ReloadCommand extends BaseCommand {
    @Command(name = "artex.reload", permission = "artex.command.reload", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Dojo.getInstance().reloadConfig();
        sender.sendMessage(CC.translate("&aSuccessfully reloaded &e" + ProjectInfo.NAME + "&a."));
    }
}