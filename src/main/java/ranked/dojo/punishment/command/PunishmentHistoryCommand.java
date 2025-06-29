package ranked.dojo.punishment.command;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ranked.dojo.punishment.menu.PunishmentSelectionMenu;

public class PunishmentHistoryCommand extends BaseCommand {
    @Override
    @Command(name = "punishmenthistory", aliases = {"phistory"}, permission = "dojo.command.punishmenthistory", description = "View a player's punishment history")
    public void onCommand(CommandArgs commandArgs) {
        if (commandArgs.getSender() instanceof Player) {
            Player sender = (Player) commandArgs.getSender();
            String[] args = commandArgs.getArgs();
            if (args.length < 1) {
                sender.sendMessage("§cUsage: /punishmenthistory <player>");
                return;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target == null || (target.getName() == null && !target.hasPlayedBefore())) {
                sender.sendMessage("§cPlayer not found.");
                return;
            }
            new PunishmentSelectionMenu(target, true).openMenu(sender);
        } else {
            commandArgs.getSender().sendMessage("§cOnly players can use this command.");
        }
    }
}
