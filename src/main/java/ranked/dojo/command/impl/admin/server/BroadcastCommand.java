package ranked.dojo.command.impl.admin.server;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.BukkitUtils;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import java.util.Arrays;

public class BroadcastCommand extends BaseCommand {
    @Command(name = "broadcast", permission = "dojo.command.broadcast", inGameOnly = false, aliases = {"bc", "alert", "announce"})
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/broadcast <message>"));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        BukkitUtils.getOnlinePlayers().forEach(player -> player.sendMessage(CC.translate("&4&lBroadcast &8- &e" + message)));
    }
}
