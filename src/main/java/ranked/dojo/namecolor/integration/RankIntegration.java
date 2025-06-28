package ranked.dojo.namecolor.integration;

import ranked.dojo.Dojo;
import ranked.dojo.profile.Profile;
import ranked.dojo.rank.Rank;
import ranked.dojo.tag.Tag;
import ranked.dojo.tag.TagService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ranked.dojo.util.BukkitUtils;

import java.util.UUID;

public class RankIntegration {
    private final Dojo plugin;
    private final TagService tagService;

    public RankIntegration(Dojo plugin) {
        this.plugin = plugin;
        this.tagService = plugin.getTagService();
    }

    public String getChatFormat(Player player) {
        try {
            Profile profile = plugin.getProfileRepository().getProfile(player.getUniqueId());
            if (profile == null) return player.getName();

            StringBuilder format = new StringBuilder();

            // 1. Rank prefix
            Rank rank = profile.getHighestRankBasedOnGrant();
            if (rank != null && rank.getPrefix() != null) {
                format.append(ChatColor.translateAlternateColorCodes('&', rank.getPrefix().trim()));
                format.append(" ");
            }

            // 2. Name with color (namecolor > rankcolor)
            format.append(getNameColor(player));
            format.append(player.getName());
            format.append(ChatColor.RESET);

            // 3. Tag
            if (tagService != null) {
                Tag tag = tagService.getActiveTag(player.getUniqueId());
                if (tag != null) {
                    format.append(" ");
                    format.append(tag.getNiceName());
                }
            }

            return format.toString();
        } catch (Exception e) {
            plugin.getLogger().warning("Error formatting chat name for " + player.getName() + ": " + e.getMessage());
            return player.getName();
        }
    }

    public String getTablistFormat(Player player) {
        try {
            // Only use rank color for tablist
            ChatColor color = getNameColor(player, true);
            return color + player.getName();
        } catch (Exception e) {
            plugin.getLogger().warning("Error formatting tablist name for " + player.getName() + ": " + e.getMessage());
            return player.getName();
        }
    }

    public String getNameColor(Player player) {
        // Check for custom name color first
        ChatColor nameColor = getSavedNameColor(player.getUniqueId());
        if (nameColor != null) {
            return nameColor.toString();
        }
        // Fall back to rank color and formatting
        return getRankColorFormat(player);
    }

    private ChatColor getNameColor(Player player, boolean tablistMode) {
        try {
            if (!tablistMode) {
                // For chat, check namecolor first
                ChatColor nameColor = getSavedNameColor(player.getUniqueId());
                if (nameColor != null) {
                    return nameColor;
                }
            }
            // For tablist or when no namecolor, use rank color
            Profile profile = plugin.getProfileRepository().getProfile(player.getUniqueId());
            if (profile != null) {
                Rank rank = profile.getHighestRankBasedOnGrant();
                if (rank != null && rank.getColor() != null) {
                    return rank.getColor();
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Error getting name color for " + player.getName() + ": " + e.getMessage());
        }
        return ChatColor.WHITE; // Default color
    }

    private String getRankColorFormat(Player player) {
        try {
            Profile profile = plugin.getProfileRepository().getProfile(player.getUniqueId());
            if (profile == null) return "";

            Rank rank = profile.getHighestRankBasedOnGrant();
            if (rank == null) return "";

            StringBuilder format = new StringBuilder();
            if (rank.isBold()) format.append(ChatColor.BOLD);
            if (rank.isItalic()) format.append(ChatColor.ITALIC);
            if (rank.getColor() != null) format.append(rank.getColor());

            return format.toString();
        } catch (Exception e) {
            plugin.getLogger().warning("Error getting rank color format for " + player.getName() + ": " + e.getMessage());
            return "";
        }
    }

    private ChatColor getSavedNameColor(UUID playerId) {
        try {
            String colorCode = plugin.getConfig().getString("namecolors." + playerId);
            return colorCode != null ? ChatColor.getByChar(colorCode.charAt(0)) : null;
        } catch (Exception e) {
            plugin.getLogger().warning("Error getting saved name color: " + e.getMessage());
            return null;
        }
    }

    public void refreshPlayerFormatting(Player player) {
        // Run on main thread to avoid async issues
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            try {
                if (player == null || !player.isOnline()) return;

                String displayName = getChatFormat(player);
                String tablistName = getTablistFormat(player);

                // Ensure names aren't too long
                if (displayName.length() > 32) displayName = displayName.substring(0, 32);
                if (tablistName.length() > 32) tablistName = tablistName.substring(0, 32);

                player.setDisplayName(displayName);
                player.setPlayerListName(tablistName);

                // Update tablist name for all online players (so everyone sees the change)
                for (Player p : BukkitUtils.getOnlinePlayers()) {
                    p.setPlayerListName(getTablistFormat(p));
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Error refreshing formatting for " +
                        (player != null ? player.getName() : "null player") + ": " + e.getMessage());
            }
        });
    }
}