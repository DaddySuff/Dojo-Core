package ranked.dojo.conversation;

import lombok.Getter;
import ranked.dojo.Dojo;
import ranked.dojo.rank.Rank;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;


@Getter
public class ConversationHandler {
    private final HashMap<UUID, UUID> conversations;
    private final FileConfiguration config;
    private final String receivedFormat;
    private final String sentFormat;

    /**
     * Constructor for the ConversationHandler class.
     *
     * @param config the configuration file
     */
    public ConversationHandler(FileConfiguration config) {
        this.conversations = new HashMap<>();
        this.config = config;
        this.receivedFormat = CC.translate(config.getString("conversation.format.received"));
        this.sentFormat = CC.translate(config.getString("conversation.format.sent"));
    }

    /**
     * Starts a conversation across two players.
     *
     * @param sender  The player who is starting the conversation.
     * @param target  The player who is being messaged.
     * @param message The message to send.
     */
    public void startConversation(UUID sender, UUID target, String message) {
        this.conversations.put(sender, target);
        this.conversations.put(target, sender);

        Player senderPlayer = Bukkit.getServer().getPlayer(sender);
        Player targetPlayer = Bukkit.getServer().getPlayer(target);

        if (targetPlayer == null && senderPlayer != null) {
            senderPlayer.sendMessage(CC.translate("&cThat player is currently offline."));
            return;
        }

        assert targetPlayer != null;
        this.sendMessage(message, targetPlayer, senderPlayer);
    }

    /**
     * Sends a message to a player with the correct format and profile color.
     *
     * @param message     The message to send.
     * @param targetPlayer The player to send the message to.
     * @param senderPlayer The player who is sending the message.
     */
    private void sendMessage(String message, Player targetPlayer, Player senderPlayer) {
        Rank senderRank = Dojo.getInstance().getProfileRepository().getProfile(senderPlayer.getUniqueId()).getHighestRankBasedOnGrant();
        Rank targetRank = Dojo.getInstance().getProfileRepository().getProfile(targetPlayer.getUniqueId()).getHighestRankBasedOnGrant();

        String senderColor = senderRank == null ? "&a" : senderRank.getColor().toString();
        String targetColor = targetRank == null ? "&a" : targetRank.getColor().toString();

        targetPlayer.sendMessage(CC.translate(this.receivedFormat
                .replace("{sender}", CC.translate(senderColor + senderPlayer.getName()))
                .replace("{message}", message)));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);

        senderPlayer.sendMessage(CC.translate(this.sentFormat
                .replace("{sender}", CC.translate(targetColor + targetPlayer.getName()))
                .replace("{message}", message)));
        senderPlayer.playSound(senderPlayer.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
    }

    /**
     * Gets the last conversant of a player.
     *
     * @param player The player to get the last conversant of.
     * @return The last conversant of the player.
     */
    public UUID getLastConversant(UUID player) {
        return this.conversations.get(player);
    }
}