package ranked.dojo.command.impl.user;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.BungeeUtil;
import ranked.dojo.util.CC;
import org.bukkit.entity.Player;


public class JoinCommand extends BaseCommand {
    @Command(name = "join")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length <1) {
            player.sendMessage(CC.translate("&cUsage: &e/join <server>"));
            return;
        }

        BungeeUtil.sendPlayer(player, args[0]);
        player.sendMessage(CC.translate("&aSending you to &e" + args[0] + "&a..."));
    }
}