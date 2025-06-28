package ranked.dojo.util;

import lombok.experimental.UtilityClass;
import ranked.dojo.Dojo;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


@UtilityClass
public class BungeeUtil {
    /**
     * Sends a player to a server
     *
     * @param player The player to send
     * @param server The server to send the player to
     */
    public void sendPlayer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(Dojo.getInstance(), "BungeeCord", b.toByteArray());
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(CC.translate("&cAn error occurred while trying to send you to the server. Please try again."));
            }
        };
        task.runTaskLater(Dojo.getInstance(), 20);
    }
}