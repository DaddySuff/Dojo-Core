package me.suff.dojo.command.impl.admin.troll;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 14:35
 */
public class LaunchCommand extends BaseCommand {
    @Override
    @Command(name = "launch", inGameOnly = false, permission = "flowercore.command.launch")
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CC.translate("&cUsage: &e/launch (player)"));
                return;
            }

            Player player = (Player) sender;
            player.setVelocity(new Vector(0, 1, 0).multiply(15));
            player.sendMessage(CC.translate("&aYou've launched yourself into the air!"));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        targetPlayer.setVelocity(new Vector(0, 1, 0).multiply(15));
        sender.sendMessage(CC.translate("&aYou've launched &e" + targetPlayer.getDisplayName() + " &ainto the air!"));
        targetPlayer.sendMessage(CC.translate("&aYou have been launched into the air by &e" + sender.getName() + " &a!"));
    }
}