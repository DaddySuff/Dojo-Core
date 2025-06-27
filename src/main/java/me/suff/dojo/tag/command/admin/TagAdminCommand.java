package me.suff.dojo.tag.command.admin;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 01/09/2024 - 20:28
 */
public class TagAdminCommand extends BaseCommand {
    @Command(name = "tagadmin", aliases = {"tagsadmin", "ta"}, permission = "dojo.ta", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lDojo &8- &7Tag Admin Commands"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate(" &6&l● &f/tagadmin create &e(name) (displayName) - &7Create a tag"));
        sender.sendMessage(CC.translate(" &6&l● &f/tagadmin delete &e(name) - &7Delete a tag"));
        sender.sendMessage(CC.translate(" &6&l● &f/tagadmin info &e(name) - &7View tag information"));
        sender.sendMessage(CC.translate(" &6&l● &f/tagadmin list - &7List all tags"));
        sender.sendMessage(CC.translate(" &6&l● &f/tagadmin setbold &e(name) (true/false) - &7Set the bold of a tag"));
        sender.sendMessage(CC.translate(" &6&l● &f/tagadmin setcolor &e(name) (color) - &7Set the color of a tag"));
        sender.sendMessage(CC.translate(" &6&l● &f/tagadmin setdisplayname &e(name) (displayname) - &7Set the display name of a tag"));
        sender.sendMessage(CC.translate(" &6&l● &f/tagadmin seticon &e(name) - &7Set the icon of a tag"));
        sender.sendMessage(CC.translate(" &6&l● &f/tagadmin setitalic &e(name) (true/false) - &7Set the italic of a tag"));
        sender.sendMessage("");
    }
}
