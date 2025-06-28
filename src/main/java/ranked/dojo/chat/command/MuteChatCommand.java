package ranked.dojo.chat.command;

import ranked.dojo.Dojo;
import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;


public class MuteChatCommand extends BaseCommand {
    @Command(name = "mutechat", permission = "dojo.command.mutechat", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Dojo.getInstance().getChatService().setChatMuted(true);
        Bukkit.broadcastMessage(CC.translate("&c&lChat has been muted by " + sender.getName() + "."));
    }
}