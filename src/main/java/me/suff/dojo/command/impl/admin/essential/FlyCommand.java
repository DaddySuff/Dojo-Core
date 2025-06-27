package me.suff.dojo.command.impl.admin.essential;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 22:40
 */
public class FlyCommand extends BaseCommand {
    @Command(name = "fly", permission = "artex.command.fly")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.setAllowFlight(!player.getAllowFlight());
            player.sendMessage(CC.translate("&eYou have " + (player.getAllowFlight() ? "&aenabled" : "&cdisabled") + " &eflight."));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        target.setAllowFlight(!target.getAllowFlight());
        target.sendMessage(CC.translate("&eYour flight has been " + (target.getAllowFlight() ? "&aenabled" : "&cdisabled") + " &eby &6" + player.getName() + "&e."));
        player.sendMessage(CC.translate("&eYou have " + (target.getAllowFlight() ? "&aenabled" : "&cdisabled") + " &6flight for &6" + target.getName() + "&e."));
    }
}
