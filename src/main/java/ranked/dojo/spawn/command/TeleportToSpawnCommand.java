package ranked.dojo.spawn.command;

import ranked.dojo.Dojo;
import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.CC;
import org.bukkit.entity.Player;


public class TeleportToSpawnCommand extends BaseCommand {
    @Command(name = "tpspawn", aliases = {"tpjoinloc", "tpjoinlocation"},permission = "dojo.spawn")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Dojo.getInstance().getSpawnHandler().teleportToSpawn(player);

        player.sendMessage(CC.translate("&eTeleported you to the spawn location."));
    }
}