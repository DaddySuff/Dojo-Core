package me.suff.dojo.spawn.command;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 23:03
 */
public class TeleportToSpawnCommand extends BaseCommand {
    @Command(name = "tpspawn", aliases = {"tpjoinloc", "tpjoinlocation"},permission = "dojo.spawn")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Dojo.getInstance().getSpawnHandler().teleportToSpawn(player);

        player.sendMessage(CC.translate("&eTeleported you to the spawn location."));
    }
}