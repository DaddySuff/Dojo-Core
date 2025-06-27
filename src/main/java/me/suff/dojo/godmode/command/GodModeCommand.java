package me.suff.dojo.godmode.command;

import me.suff.dojo.Dojo;
import me.suff.dojo.api.command.BaseCommand;
import me.suff.dojo.api.command.CommandArgs;
import me.suff.dojo.api.command.annotation.Command;
import me.suff.dojo.godmode.GodModeRepository;
import me.suff.dojo.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Artex
 * @date 03/09/2024 - 22:46
 */
public class GodModeCommand extends BaseCommand {
    @Command(name = "godmode", permission = "dojo.godmode")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        GodModeRepository godModeRepository = Dojo.getInstance().getGodModeRepository();

        if (args.length < 1) {
            if (!godModeRepository.isGodModeEnabled(player)) {
                godModeRepository.enableGodMode(player);
                player.sendMessage(CC.translate("&aGod mode has been enabled."));
            } else {
                godModeRepository.disableGodMode(player);
                player.sendMessage(CC.translate("&cGod mode has been disabled."));
            }

            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        if (!godModeRepository.isGodModeEnabled(target)) {
            godModeRepository.enableGodMode(target);
            player.sendMessage(CC.translate("&aGod mode has been enabled for &e" + target.getName() + "."));
            target.sendMessage(CC.translate("&aGod mode has been enabled."));
        } else {
            godModeRepository.disableGodMode(target);
            player.sendMessage(CC.translate("&cGod mode has been disabled for &e" + target.getName() + "."));
            target.sendMessage(CC.translate("&cGod mode has been disabled."));
        }
    }
}