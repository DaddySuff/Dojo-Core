package ranked.dojo.command.impl.admin.server;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.BukkitUtils;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;


public class AlertCommand extends BaseCommand {
    @Command(name = "alert", permission = "dojo.command.alert", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: &e/alert <message>"));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        BukkitUtils.getOnlinePlayers().forEach(player -> player.sendMessage(CC.translate("&4&lAlert &8- &e" + message)));
    }
}