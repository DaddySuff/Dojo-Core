package ranked.dojo.command.impl;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.CC;
import ranked.dojo.util.ProjectInfo;
import org.bukkit.command.CommandSender;


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
