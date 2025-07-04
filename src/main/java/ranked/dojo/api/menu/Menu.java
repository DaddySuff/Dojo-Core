package ranked.dojo.api.menu;

import lombok.Getter;
import lombok.Setter;
import ranked.dojo.Dojo;
import ranked.dojo.api.menu.button.PageGlassButton;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class Menu {

    public static Map<String, Menu> currentlyOpenedMenus = new HashMap<>();

    @Getter
    protected Dojo ricardo = Dojo.getInstance();
    private Map<Integer, Button> buttons = new HashMap<>();
    private boolean autoUpdate = false;
    private boolean updateAfterClick = true;
    private boolean closedByMenu = false;
    private boolean placeholder = false;
    private Button placeholderButton = Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 15, " ");

    private ItemStack createItemStack(Player player, Button button) {
        ItemStack item = button.getButtonItem(player);

        if (item.getType() != Material.SKULL_ITEM) {
            ItemMeta meta = item.getItemMeta();

            if (meta != null && meta.hasDisplayName()) {
                meta.setDisplayName(meta.getDisplayName() + "§b§c§d§e");
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public void openMenu(final Player player) {
        this.buttons = this.getButtons(player);

        Inventory inventory;
        int size = this.getSize() == -1 ? this.size(this.buttons) : this.getSize();
        String title = CC.translate(this.getTitle(player));

        if (title.length() > 32) {
            title = title.substring(0, 32);
        }

        inventory = Bukkit.createInventory(player, size, title);

        inventory.setContents(new ItemStack[inventory.getSize()]);

        for (Map.Entry<Integer, Button> buttonEntry : this.buttons.entrySet()) {
            inventory.setItem(buttonEntry.getKey(), createItemStack(player, buttonEntry.getValue()));
        }

        if (this.isPlaceholder()) {
            for (int index = 0; index < size; index++) {
                if (this.buttons.get(index) == null) {
                    this.buttons.put(index, this.placeholderButton);
                    inventory.setItem(index, this.placeholderButton.getButtonItem(player));
                }
            }
        }

        player.openInventory(inventory);

        currentlyOpenedMenus.put(player.getName(), this);

        this.onOpen(player);
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = 0;

        for (int buttonValue : buttons.keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        return (int) (Math.ceil((highest + 1) / 9D) * 9D);
    }

    public void addBorder(Map<Integer, Button> buttons, byte data, int rows) {
        for (int i = 0; i < 9; i++) {
            buttons.putIfAbsent(i, Button.placeholder(Material.STAINED_GLASS_PANE, data, ""));
        }

        for (int i = 0; i < 9; i++) {
            buttons.putIfAbsent((rows - 1) * 9 + i, Button.placeholder(Material.STAINED_GLASS_PANE, data, ""));
        }

        for (int i = 0; i < rows; i++) {
            buttons.putIfAbsent(i * 9, Button.placeholder(Material.STAINED_GLASS_PANE, data, ""));
            buttons.putIfAbsent(i * 9 + 8, Button.placeholder(Material.STAINED_GLASS_PANE, data, ""));
        }
    }

    public void addGlassHeader(Map<Integer, Button> buttons, int durability) {
        int maxSlot = getSize() - 1;
        for (int i = 1; i < 9 && i <= maxSlot; i++) {
            if (i != 8) {
                buttons.put(i, new PageGlassButton(durability));
            }
        }
    }

    public void addGlass(Map<Integer, Button> buttons, int durability) {
        for (int slot = 0; slot < getSize(); slot++) {
            if (!buttons.containsKey(slot)) {
                buttons.put(slot, new PageGlassButton(durability));
            }
        }
    }

    public int getSize() {
        return -1;
    }

    public int getSlot(int x, int y) {
        return ((9 * y) + x);
    }

    public abstract String getTitle(Player player);

    public abstract Map<Integer, Button> getButtons(Player player);

    public void onOpen(Player player) {
    }

    public void onClose(Player player) {
    }

}
