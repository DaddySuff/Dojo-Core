package ranked.dojo.instance.menu;

import ranked.dojo.api.menu.Button;
import ranked.dojo.api.menu.Menu;
import ranked.dojo.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ConfirmMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Are you sure?";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(12, new CancelButton());
        buttons.put(14, new ConfirmButton());

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    private static class ConfirmButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.WOOL)
                    .name("&a&lConfirm")
                    .durability(5)
                    .lore(Collections.singletonList("&aClick to confirm the shutdown."))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Bukkit.shutdown();
        }
    }

    private static class CancelButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.WOOL)
                    .name("&4&lCancel")
                    .durability(14)
                    .lore(Collections.singletonList("&cClick to cancel the shutdown."))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            new InstanceMenu().openMenu(player);
        }
    }
}