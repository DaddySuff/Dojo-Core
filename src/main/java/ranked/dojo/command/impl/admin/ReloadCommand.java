package ranked.dojo.command.impl.admin;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.Dojo;
import ranked.dojo.util.CC;
import ranked.dojo.util.ProjectInfo;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends BaseCommand {
    @Command(name = "dreload", aliases = {"dojoreload"}, permission = "dojo.command.reload", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Dojo.getInstance().reloadConfig();
        sender.sendMessage(CC.translate("&aSuccessfully reloaded &e" + ProjectInfo.NAME + "&a."));
    }
}