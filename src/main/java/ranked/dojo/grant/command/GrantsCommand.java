package ranked.dojo.grant.command;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.grant.menu.grants.GrantsMenu;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class GrantsCommand extends BaseCommand {
    @Command(name = "grants", permission = "dojo.command.grants")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/grants (player)"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        new GrantsMenu(target, false).openMenu(player);
    }
}