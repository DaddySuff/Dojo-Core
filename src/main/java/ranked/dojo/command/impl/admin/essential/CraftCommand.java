package ranked.dojo.command.impl.admin.essential;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.CC;
import org.bukkit.entity.Player;

public class CraftCommand extends BaseCommand {
    @Command(name = "craft", permission = "dojo.command.craft")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(CC.translate("&eNo way you're that lazy to craft a workbench. I'll be nice and open it for you. Don't forget to say thank you in chat!"));
        player.openWorkbench(player.getLocation(), true);
    }
}
