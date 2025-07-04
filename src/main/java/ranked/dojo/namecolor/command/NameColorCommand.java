package ranked.dojo.namecolor.command;

import ranked.dojo.Dojo;
import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.profile.Profile;
import ranked.dojo.rank.Rank;
import org.bukkit.entity.Player;

public class NameColorCommand extends BaseCommand {

    @Command(name = "namecolor", description = "Open the name color menu", usage = "/namecolor", inGameOnly = true)
    @Override
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        if (!player.hasPermission("dojo.namecolor")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return;
        }

        // Rank color restriction logic
        Profile profile = Dojo.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile != null) {
            Rank rank = profile.getHighestRankBasedOnGrant();
            if (rank != null && rank.getColor() != null) {
                String colorCode = rank.getColor().toString();
                String bold = rank.isBold() ? org.bukkit.ChatColor.BOLD.toString() : "";
                String italic = rank.isItalic() ? org.bukkit.ChatColor.ITALIC.toString() : "";
                String formattedColor = bold + italic + colorCode;
                String rankName = rank.getName();

                if (!player.hasPermission("dojo.namecolor.override")) {
                    player.sendMessage(String.format(
                            "§cYour name color is locked to %s%s§c by your rank!",
                            formattedColor,
                            rankName
                    ));
                    return;
                } else {
                    player.sendMessage(String.format(
                            "§6Warning: Your rank normally locks you to %s%s§6, but you can override it.",
                            formattedColor,
                            rankName
                    ));
                }
            }
        }

        Dojo.getInstance().getNameColorMenu().openMenu(player);
    }
}
