package me.suff.dojo.chat.listener;

import lombok.Getter;
import me.suff.dojo.Dojo;
import me.suff.dojo.namecolor.integration.RankIntegration;
import me.suff.dojo.profile.Profile;
import me.suff.dojo.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.suff.dojo.tag.Tag;

/**
 * @author Emmy
 * @project Artex
 * @date 28/08/2024 - 16:28
 */
@Getter
public class ChatListener implements Listener {

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = Dojo.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        String message = event.getMessage();

        String prefixRaw = profile.getHighestRankBasedOnGrant().getPrefix();
        String rankPrefix = prefixRaw != null && !prefixRaw.trim().isEmpty()
                ? CC.translate(prefixRaw)
                : "";

        String suffixRaw = profile.getHighestRankBasedOnGrant().getSuffix();
        String rankSuffix = suffixRaw != null && !suffixRaw.trim().isEmpty()
                ? " " + CC.translate(suffixRaw)
                : "";

        ChatColor rankColor = profile.getHighestRankBasedOnGrant().getColor();
        boolean translate = player.hasPermission("dojo.chat.color");
        String colon = CC.translate("&f: &r");

        Tag activeTag = Dojo.getInstance().getTagService().getActiveTag(player.getUniqueId());
        String tag = activeTag == null ? "" : " " + CC.translate(activeTag.getNiceName());

        // Use RankIntegration to get the correct name color (namecolor > rankcolor)
        RankIntegration rankIntegration = new RankIntegration(Dojo.getInstance());
        String nameColor = rankIntegration.getNameColor(player);
        String chatName = nameColor + player.getName();
        String format = (rankPrefix.isEmpty() ? "" : rankPrefix) + chatName + rankSuffix + tag + colon + (translate ? CC.translate(message) : message);
        event.setFormat(format);

        if (Dojo.getInstance().getChatService().isChatMuted()) {
            if (player.hasPermission("dojo.bypass.mutechat")) {
                return;
            }

            event.setCancelled(true);
            player.sendMessage(CC.translate("&cChat is currently muted."));
        }
    }
}