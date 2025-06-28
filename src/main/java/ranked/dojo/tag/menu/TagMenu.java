package ranked.dojo.tag.menu;

import lombok.AllArgsConstructor;
import ranked.dojo.Dojo;
import ranked.dojo.api.menu.Button;
import ranked.dojo.api.menu.pagination.PaginatedMenu;
import ranked.dojo.namecolor.integration.RankIntegration;
import ranked.dojo.profile.Profile;
import ranked.dojo.rank.Rank;
import ranked.dojo.tag.Tag;
import ranked.dojo.util.CC;
import ranked.dojo.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;


@AllArgsConstructor
public class TagMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lSelect a tag";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        addGlassHeader(buttons, 15);

        buttons.put(4, new RemoveCurrentTagButton());

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Tag> sortedTags = Dojo.getInstance().getTagService().getTags()
                .stream()
                .sorted(Comparator.comparing(Tag::getName))
                .collect(Collectors.toList());

        int slot = 0;

        for (Tag tag : sortedTags) {
            buttons.put(slot++, new TagButton(tag));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class TagButton extends Button {

        private Tag tag;

        @Override
        public ItemStack getButtonItem(Player player) {
            // Only apply color to the item name, not bold/italic
            String coloredName = (tag.getColor() != null ? tag.getColor() : "") + tag.getDisplayName();

            List<String> lore = Arrays.asList(
                    "",
                    "&fAppearance:&r",
                    getRankPrefixAndNameColor(player) + getNameColor(player) + player.getName() + " " + tag.getNiceName() + "&7: &ftag&r",
                    "",
                    "&aClick to select this tag."
            );

            return new ItemBuilder(tag.getIcon())
                    .name(coloredName)
                    .lore(lore)
                    .durability(tag.getDurability())
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (!player.hasPermission("artex.tag." + tag.getName())) {
                player.sendMessage(CC.translate("&cYou don't own this tag."));
                return;
            }

            Profile profile = Dojo.getInstance().getProfileRepository().getProfileWithNoAdding(player.getUniqueId());
            Tag activeTag = Dojo.getInstance().getTagService().getActiveTag(profile.getUuid());
            if (activeTag != null && activeTag.equals(tag)) {
                player.sendMessage(CC.translate("&cYou already have this tag selected."));
                return;
            }
            Dojo.getInstance().getTagService().setActiveTag(profile.getUuid(), tag.getName());
            player.sendMessage(CC.translate("&aYou have selected the &e" + tag.getDisplayName() + "&a tag."));
            player.closeInventory();
        }
    }

    @AllArgsConstructor
    private static class RemoveCurrentTagButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Dojo.getInstance().getProfileRepository().getProfileWithNoAdding(player.getUniqueId());
            Tag activeTag = Dojo.getInstance().getTagService().getActiveTag(profile.getUuid());
            String tagName = activeTag == null ? "None" : activeTag.getName();
            List<String> lore = Arrays.asList(
                    "",
                    activeTag == null ? "&cYou do not have a tag selected." : "&cClick to remove your current tag."
            );
            return new ItemBuilder(Material.REDSTONE)
                    .name(activeTag == null ? "&cNone selected." : "&aCurrent tag: " + activeTag.getColor() + activeTag.getName() + "&r")
                    .lore(lore)
                    .build();
        }
        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            Profile profile = Dojo.getInstance().getProfileRepository().getProfileWithNoAdding(player.getUniqueId());
            Tag activeTag = Dojo.getInstance().getTagService().getActiveTag(profile.getUuid());
            if (activeTag == null) {
                player.sendMessage(CC.translate("&cYou do not have a tag selected."));
                return;
            }
            Dojo.getInstance().getTagService().clearActiveTag(profile.getUuid());
            player.sendMessage(CC.translate("&cYou have removed your current tag."));
            player.closeInventory();
        }
    }

    @Override
    public void openMenu(Player player) {
        super.openMenu(player);
    }

    private static String getRankPrefixAndNameColor(Player player) {
        Profile profile = Dojo.getInstance().getProfileRepository().getProfileWithNoAdding(player.getUniqueId());
        if (profile == null) return "";
        Rank rank = profile.getHighestRankBasedOnGrant();
        if (rank == null) return "";
        String prefix = rank.getPrefix() != null ? rank.getPrefix() : "";
        String nameColor = rank.getColor() != null ? rank.getColor().toString() : "";
        if (!prefix.isEmpty()) {
            return prefix + " " + nameColor;
        } else {
            return nameColor;
        }

    }

    private static String getNameColor(Player player) {
        Profile profile = Dojo.getInstance().getProfileRepository().getProfileWithNoAdding(player.getUniqueId());
        if (profile == null) return "";
        String colorCode = Dojo.getInstance().getConfig().getString("namecolors." + player.getUniqueId());
        if (colorCode != null && !colorCode.isEmpty()) {
            ChatColor color = ChatColor.getByChar(colorCode.charAt(0));
            if (color != null) return color.toString();
        }
        if (profile.getRank() != null && profile.getRank().getColor() != null) {
            StringBuilder format = new StringBuilder();
            if (profile.getRank().isBold()) format.append(ChatColor.BOLD);
            if (profile.getRank().isItalic()) format.append(ChatColor.ITALIC);
            format.append(profile.getRank().getColor());
            return format.toString();
        }
        return "";
    }
}