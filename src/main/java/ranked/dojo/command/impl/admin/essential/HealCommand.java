package ranked.dojo.command.impl.admin.essential;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HealCommand extends BaseCommand {
    @Command(name = "heal", permission = "dojo.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.sendMessage(CC.translate("&eYou have been healed."));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        targetPlayer.setHealth(20.0);
        targetPlayer.setFoodLevel(20);
        targetPlayer.setFireTicks(0);
        player.sendMessage(CC.translate("&eYou have healed &6" + targetPlayer.getName() + "&e."));
        targetPlayer.sendMessage(CC.translate("&eYou have been healed by" + player + "."));
    }
}
