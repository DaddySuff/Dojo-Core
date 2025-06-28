package ranked.dojo.command.impl.user;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import org.bukkit.entity.Player;
import ranked.dojo.util.CC;
import ranked.dojo.util.BukkitUtils;

/**
 * @author curxxed
 * @date 6/28/2025
 * @project Dojo
 */
public class PingCommand extends BaseCommand {
    @Override
    @Command(name =  "ping", aliases = {"pong"})
    public void onCommand(CommandArgs commandArgs) {
        if (!(commandArgs.getSender() instanceof Player)) {
            commandArgs.getSender().sendMessage(CC.translate(CC.PREFIX + "&cThis command can only be used by players."));
            return;
        }
        Player player = (Player) commandArgs.getSender();
        int ping = getPing(player);
        if (ping == -1) {
            player.sendMessage(CC.translate(CC.PREFIX + "&cCould not retrieve your ping."));
            return;
        }
        player.sendMessage(CC.translate(CC.PREFIX + "&eYour ping is &a" + ping + "ms&e."));
    }

    private static int getPing(Player player) {
        try {
            Class<?> craftPlayerClass = BukkitUtils.getCraftBukkitClass("entity.CraftPlayer");
            if (craftPlayerClass == null) {
                BukkitUtils.log("Could not find CraftPlayer class for ping retrieval.");
                return -1;
            }
            Object craftPlayer = craftPlayerClass.cast(player);
            Object handle = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
            return (int) handle.getClass().getField("ping").get(handle);
        } catch (Exception e) {
            BukkitUtils.log("Failed to get ping for player: " + player.getName() + ". " + e.getMessage());
            return -1;
        }
    }
}
