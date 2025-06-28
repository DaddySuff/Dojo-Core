package ranked.dojo.grant.command;

import ranked.dojo.Dojo;
import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.grant.menu.grant.GrantMenu;
import ranked.dojo.namecolor.integration.RankIntegration;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;


public class GrantCommand extends BaseCommand {
    @Command(name = "grant", permission = "dojo.command.grant", aliases = {"setrank"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: &e/grant (player) &7[reason]"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        String reason;
        if (args.length > 2) {
            reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            new GrantMenu(target, reason).openMenu(player);
            return;
        }

        reason = Dojo.getInstance().getConfig().getString("grant.default-reason");
        new GrantMenu(target, reason).openMenu(player);
    }
}