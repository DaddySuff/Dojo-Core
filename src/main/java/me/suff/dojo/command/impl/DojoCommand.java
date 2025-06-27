package me.suff.dojo.command.impl;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import me.suff.dojo.util.ProjectInfo;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 28/08/2024 - 23:10
 */
public class DojoCommand extends BaseCommand {
    @Command(name = "dojo", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("  &6&lDojo Core &8- &7Information"));
        sender.sendMessage(CC.translate("&r"));
        sender.sendMessage(CC.translate("   &6&l● &fAuthor: &e" + ProjectInfo.AUTHORS));
        sender.sendMessage(CC.translate("   &6&l● &fVersion: &e1.2.6"));
        sender.sendMessage(CC.translate("   &6&l● &fDescription: &eServer management plugin"));
        sender.sendMessage(CC.translate("&7&oOriginally Artex Core, Reorganised for Ranked Dojo"));
        sender.sendMessage("");
    }
}
