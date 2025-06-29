package ranked.dojo.punishment.menu;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ranked.dojo.api.menu.Button;
import ranked.dojo.api.menu.Menu;
import ranked.dojo.punishment.enums.EnumPunishmentType;
import ranked.dojo.util.ItemBuilder;

import java.util.HashMap;
import java.util.Map;

public class PunishmentSelectionMenu extends Menu {
    private final OfflinePlayer target;
    private boolean showActive = true;

    public PunishmentSelectionMenu(OfflinePlayer target, boolean showActive) {
        this.target = target;
        this.showActive = showActive;
    }

    @Override
    public String getTitle(Player player) {
        return "Select Punishment Type";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(10, new TypeButton(EnumPunishmentType.WARN));
        buttons.put(12, new TypeButton(EnumPunishmentType.KICK));
        buttons.put(14, new TypeButton(EnumPunishmentType.MUTE));
        buttons.put(16, new TypeButton(EnumPunishmentType.BAN));
        buttons.put(28, new TypeButton(EnumPunishmentType.BLACKLIST));
        this.addBorder(buttons, (byte) 15, 5);
        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    private class TypeButton extends Button {
        private final EnumPunishmentType type;
        public TypeButton(EnumPunishmentType type) {
            this.type = type;
        }
        @Override
        public ItemStack getButtonItem(Player player) {
            Material mat;
            String name;
            switch (type) {
                case WARN: mat = Material.PAPER; name = "&eWarn"; break;
                case KICK: mat = Material.BOOK; name = "&cKick"; break;
                case MUTE: mat = Material.BOOK_AND_QUILL; name = "&6Mute"; break;
                case BAN: mat = Material.IRON_FENCE; name = "&4Ban"; break;
                case BLACKLIST: mat = Material.BEDROCK; name = "&8Blacklist"; break;
                default: mat = Material.BARRIER; name = "Unknown"; break;
            }
            return new ItemBuilder(mat).name(name).lore("", "&7Click to view this punishment type.").build();
        }
        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            new PunishmentMenu(target, showActive, type).openMenu(player);
        }
    }
}
