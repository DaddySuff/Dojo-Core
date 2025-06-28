package ranked.dojo.command.impl.admin.essential;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.CC;
import org.bukkit.entity.Player;

public class RepairCommand extends BaseCommand {
    @Command(name = "repair", permission = "dojo.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.getItemInHand() == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item to repair!"));
            return;
        }

        player.getItemInHand().setDurability((short) 0);
        player.sendMessage(CC.translate("&aSuccessfully repaired the &e" + player.getItemInHand().getItemMeta().getDisplayName() + "&a!"));
    }
}
