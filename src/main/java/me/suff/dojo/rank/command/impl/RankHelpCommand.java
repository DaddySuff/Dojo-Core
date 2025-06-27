package me.suff.dojo.rank.command.impl;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Artex
 * @date 28/09/2024 - 19:49
 */
public class RankHelpCommand extends BaseCommand {
    @Command(name = "rank.help", permission = "dojo.rank.help", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lDojo &8- &7Rank Instructions"));
        sender.sendMessage("");

        String[] messages = {
                "&e&lCreating a Rank",
                " &fUse &e/rank create (name) (doPrefix)",
                " &fAppend 'true' in create command to set a prefix.",
                " &fChange prefix later with &e/rank setprefix &fcommand.",
                "",
                "",
                "",
                "&e&lSetting a Rank Prefix",
                " &fUse &e/rank setprefix (name) (prefix)",
                " &fPrefix can contain color codes.",
                " &fExample: &e/rank setprefix &fTestRank &7[&cTest&7]",
                "",
                "",
                "",
                "&e&lSetting a Rank Color",
                " &fUse &e/rank setcolor (name) (color)",
                " &fExample: &e/rank setcolor &fTestRank &cRED",
                "",
                "",
                "",
                "&e&lSetting a Rank Weight",
                " &fUse &e/rank setweight (name) (weight)",
                " &fExample: &e/rank setweight &fTestRank &61",
                "",
                "",
                "&cFor more info, contact the plugin developer.",
                "",
                ""
        };

        for (String message : messages) {
            sender.sendMessage(CC.translate(message));
        }
        sender.sendMessage("");
    }
}
