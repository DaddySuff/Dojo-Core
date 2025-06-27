package me.suff.dojo.command.impl.admin.essential;

import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 14:30
 */
public class CraftCommand extends BaseCommand {
    @Command(name = "craft", permission = "dojo.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(CC.translate("&eNo way you're that lazy to craft a workbench. I'll be nice and open it for you. Don't forget to say thank you in chat!"));
        player.openWorkbench(player.getLocation(), true);
    }
}
