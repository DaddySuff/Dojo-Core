package me.suff.dojo.spawn.command;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 22:56
 */
public class SetJoinLocationCommand extends BaseCommand {
    @Command(name = "setjoinlocation", permission = "dojo.sjlocation")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Location location = player.getLocation();
        Dojo.getInstance().getSpawnHandler().saveLocation(location);

        player.sendMessage(CC.translate("&eSuccessfully set the join location."));
        player.sendMessage(CC.translate(" &7&o(" + location.getBlockX() + "&8&o, &7&o" + location.getBlockY() + "&8&o, &7&o" + location.getBlockZ() + location.getYaw() + "&8&o, &7&o" + location.getPitch() + " &8&oin &7&o" + location.getWorld().getName() + "&8&o)"));
    }
}