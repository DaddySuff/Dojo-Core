package me.suff.dojo.grant.menu.grant;

import lombok.AllArgsConstructor;
import me.suff.dojo.Dojo;
import me.suff.dojo.api.menu.Button;
import me.suff.dojo.api.menu.pagination.PaginatedMenu;
import me.suff.dojo.profile.Profile;
import me.suff.dojo.rank.Rank;
import me.suff.dojo.util.CC;
import me.suff.dojo.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Artex
 * @date 29/08/2024 - 09:26
 */
@AllArgsConstructor
public class GrantMenu extends PaginatedMenu {
    private OfflinePlayer target;
    private String reason;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Grant " + this.target.getName();
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        List<Rank> sortedRanks = Dojo.getInstance().getRankService().getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .collect(Collectors.toList());

        for (int i = 0; i < sortedRanks.size(); i++) {
            Rank rank = sortedRanks.get(i);
            buttons.put(i, new GrantButton(this.target, rank, this.reason));
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6; // 36 slots (0-35)
    }

    @AllArgsConstructor
    private static class GrantButton extends Button {
        private OfflinePlayer target;
        private Rank rank;
        private String reason;

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = Arrays.asList(
                    "",
                    this.rank.getColor() + "Rank info",
                    " &6&l● &fName: &e" + this.rank.getRankWithColor(),
                    " &6&l● &fWeight: &e" + this.rank.getColor() + this.rank.getWeight(),
                    " &6&l● &fPrefix: &e" + this.rank.getPrefix(),
                    " &6&l● &fSuffix: &e" + this.rank.getSuffix(),
                    " &6&l● &fBold: &e" + this.rank.getColor() + this.rank.isBold(),
                    " &6&l● &fItalic: &e" + this.rank.getColor() + this.rank.isItalic(),
                    " &6&l● &fColor: &e" + this.rank.getColor() + this.rank.getColor().name(),
                    "",
                    this.rank.isDefaultRank() ? "&cYou cannot grant the default rank." : "&aClick to grant the rank."
            );
            return new ItemBuilder(Material.PAPER)
                    .name(this.rank.getRankWithColor())
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            if (this.rank.isDefaultRank()) {
                player.sendMessage(CC.translate("&cYou cannot grant the default rank."));
                return;
            }

            Profile profile = Dojo.getInstance().getProfileRepository().getProfileWithNoAdding(this.target.getUniqueId());
            if (profile.getGrants().stream().anyMatch(grant -> grant.getRank().equals(this.rank) && grant.isActive())) {
                player.sendMessage(CC.translate("&e" + this.target.getName() + " &calready has that rank granted."));
                return;
            }

            new GrantDurationMenu(this.target, this.rank, this.reason).openMenu(player);
        }
    }
}
