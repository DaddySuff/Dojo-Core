package ranked.dojo.grant.menu.grant;

import lombok.AllArgsConstructor;
import ranked.dojo.api.menu.Button;
import ranked.dojo.api.menu.Menu;
import ranked.dojo.rank.Rank;
import ranked.dojo.util.CC;
import ranked.dojo.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
public class GrantDurationMenu extends Menu {
    private OfflinePlayer target;
    private Rank rank;
    private String reason;

    @Override
    public String getTitle(Player player) {
        return "Select a duration";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(21, new GrantSelectDurationButton(this.target, this.rank, this.reason));
        buttons.put(23, new GrantPermanentButton(this.target, this.rank, this.reason));

        this.addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    @AllArgsConstructor
    private static class GrantPermanentButton extends Button {
        private OfflinePlayer target;
        private Rank rank;
        private String reason;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.WATCH)
                    .name("&6&lPermanent")
                    .lore("", CC.translate("&aClick to set the duration to &epermanent&a."), "")
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            new GrantConfirmMenu(this.target, this.rank, this.reason, 0, true).openMenu(player);
        }
    }

    @AllArgsConstructor
    private static class GrantSelectDurationButton extends Button {
        private final OfflinePlayer target;
        private final Rank rank;
        private final String reason;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.ITEM_FRAME)
                    .name("&6&l30 Days")
                    .lore("", "&bClick to set the duration&e.", "")
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            long durationInMillis = 30L * 24 * 60 * 60 * 1000;

            new GrantConfirmMenu(this.target, this.rank, this.reason, durationInMillis, false).openMenu(player);
        }
    }
}