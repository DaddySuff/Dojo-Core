package ranked.dojo.conversation.command;

import ranked.dojo.Dojo;
import net.curxxed.dev.CommandAPI.BaseCommand;
import net.curxxed.dev.CommandAPI.Command;
import net.curxxed.dev.CommandAPI.CommandArgs;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;


public class ReplyCommand extends BaseCommand {
    @Command(name = "reply", aliases = {"r"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        UUID lastConversantUUID = Dojo.getInstance().getConversationHandler().getLastConversant(player.getUniqueId());
        Player lastConversant = Bukkit.getServer().getPlayer(lastConversantUUID);

        if (args.length < 1) {
            if (lastConversant == null) {
                player.sendMessage(CC.translate("&cYou haven't been in a conversation yet."));
                return;
            }

            player.sendMessage(CC.translate("&6You've last messaged: &e" + Dojo.getInstance().getProfileRepository().getProfile(lastConversantUUID).getHighestRankBasedOnGrant().getColor() + lastConversant.getName()));
            return;
        }

        if (lastConversantUUID == null) {
            player.sendMessage(CC.translate("&cYou haven't been in a conversation yet."));
            return;
        }

        Player target = Bukkit.getServer().getPlayer(lastConversantUUID);
        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        Dojo.getInstance().getConversationHandler().startConversation(player.getUniqueId(), target.getUniqueId(), message);
    }
}