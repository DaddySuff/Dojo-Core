package ranked.dojo.chat.command;

import ranked.dojo.Dojo;
import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;


public class UnMuteChatCommand extends BaseCommand {
    @Command(name = "unmutechat", permission = "dojo.command.unmutechat", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Dojo.getInstance().getChatService().setChatMuted(false);
        Bukkit.broadcastMessage(CC.translate("&a&lChat has been unmuted by " + sender.getName() + "."));
    }
}