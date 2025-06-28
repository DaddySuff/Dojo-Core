package ranked.dojo.instance.command;

import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.instance.menu.InstanceMenu;
import org.bukkit.entity.Player;


public class InstanceCommand extends BaseCommand {
    @Command(name = "instance", permission = "dojo.command.instance")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new InstanceMenu().openMenu(player);
    }
}
