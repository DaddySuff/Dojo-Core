package ranked.dojo.grant.menu.grant;

import lombok.AllArgsConstructor;
import ranked.dojo.Dojo;
import ranked.dojo.api.menu.Button;
import ranked.dojo.api.menu.Menu;
import ranked.dojo.grant.Grant;
import ranked.dojo.locale.Locale;
import ranked.dojo.namecolor.integration.RankIntegration;
import ranked.dojo.profile.Profile;
import ranked.dojo.rank.Rank;
import ranked.dojo.util.CC;
import ranked.dojo.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
public class GrantConfirmMenu extends Menu {
    private OfflinePlayer target;
    private Rank rank;
    private String reason;
    private double duration;
    private boolean permanent;

    @Override
    public String getTitle(Player player) {
        return "Confirm Grant";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(12, new CancelButton());
        buttons.put(14, new ConfirmButton(this.target, this.rank, this.reason, this.duration, this.permanent));

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @AllArgsConstructor
    private static class ConfirmButton extends Button {
        private OfflinePlayer target;
        private Rank rank;
        private String reason;
        private double duration;
        private boolean permanent;


        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.EMERALD)
                    .name("&aConfirm")
                    .lore(Collections.singletonList("&7Click to confirm the grant of &e" + this.target.getName() + " &7the rank &e" + this.rank.getName() + "&7."))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            Profile profile = Dojo.getInstance().getProfileRepository().getProfile(this.target.getUniqueId());

            Grant grant = new Grant();
            grant.setAddedBy(player.getName());
            grant.setAddedAt(System.currentTimeMillis());
            grant.setAddedOn(Locale.SERVER_NAME.getString());
            grant.setReason(this.reason);
            grant.setPermanent(this.permanent);
            grant.setRank(this.rank.getName());
            grant.setActive(true);

            if (!this.permanent) {
                grant.setDuration((long) this.duration);
            } else {
                grant.setDuration(0);
            }

            profile.getGrants().add(grant);
            profile.save();

            player.closeInventory();
            player.sendMessage(CC.translate("&aYou have granted &e" + this.target.getName() + " &athe rank &6" + this.rank.getName() + "&a."));
            if (this.target.isOnline()) {
                this.target.getPlayer().sendMessage(CC.translate("&aYou have been granted the rank &e" + this.rank.getName() + "&a."));
                new RankIntegration(Dojo.getInstance()).refreshPlayerFormatting(this.target.getPlayer());
            }
        }
    }

    @AllArgsConstructor
    private static class CancelButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.REDSTONE_BLOCK)
                    .name("&cCancel")
                    .lore(Collections.singletonList("&7Click to cancel the grant."))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            player.closeInventory();
            player.sendMessage(CC.translate("&cYou have cancelled the grant."));
        }
    }
}