package ranked.dojo.namecolor.menu;

import ranked.dojo.Dojo;
import ranked.dojo.namecolor.integration.RankIntegration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;

public class NameColorMenu implements Listener {

    private final Dojo plugin;
    private final String GUI_TITLE = "§6§lChoose A Color";
    private RankIntegration rankIntegration;

    public NameColorMenu(Dojo plugin, RankIntegration rankIntegration) {
        this.plugin = plugin;
        this.rankIntegration = rankIntegration;
    }

    public void openMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, GUI_TITLE);

        // Fill empty slots with gray glass
        ItemStack grayGlass = createItem(Material.STAINED_GLASS_PANE, 15, " ", null);
        for (int i = 0; i < 27; i++) {
            gui.setItem(i, grayGlass);
        }

        // Add color options in the middle row (slots 10-16)
        gui.setItem(10, createColorItem(Material.LEATHER_CHESTPLATE, "§cRed", ChatColor.RED, Color.fromRGB(255, 85, 85)));
        gui.setItem(11, createColorItem(Material.LEATHER_CHESTPLATE, "§9Blue", ChatColor.BLUE, Color.fromRGB(85, 85, 255)));
        gui.setItem(12, createColorItem(Material.LEATHER_CHESTPLATE, "§bAqua", ChatColor.AQUA, Color.fromRGB(85, 255, 255)));
        gui.setItem(13, createColorItem(Material.LEATHER_CHESTPLATE, "§5Purple", ChatColor.DARK_PURPLE, Color.fromRGB(170, 0, 170)));
        gui.setItem(14, createColorItem(Material.LEATHER_CHESTPLATE, "§dPink", ChatColor.LIGHT_PURPLE, Color.fromRGB(255, 150, 200)));
        gui.setItem(15, createColorItem(Material.LEATHER_CHESTPLATE, "§6Gold", ChatColor.GOLD, Color.fromRGB(255, 170, 0)));
        gui.setItem(16, createColorItem(Material.LEATHER_CHESTPLATE, "§aLime", ChatColor.GREEN, Color.fromRGB(128, 255, 128)));

        player.openInventory(gui);
    }

    private ItemStack createColorItem(Material material, String name, ChatColor chatColor, Color armorColor) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList("§7§oClick to select this color"));

        if (meta instanceof LeatherArmorMeta) {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
            leatherMeta.setColor(armorColor);
        }

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createItem(Material material, int data, String name, String lore) {
        ItemStack item = new ItemStack(material, 1, (short) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null) {
            meta.setLore(Arrays.asList(lore));
        }
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().getTitle().equals(GUI_TITLE)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() != Material.LEATHER_CHESTPLATE) {
            return;
        }

        ChatColor selectedColor = getColorFromSlot(event.getSlot());
        if (selectedColor == null) {
            return;
        }

        // Check if player has permission for this specific color
        String colorName = selectedColor.name().toLowerCase();
        if (!player.hasPermission("dojo.namecolor." + colorName) && !player.hasPermission("dojo.namecolor.*")) {
            player.sendMessage("§cYou don't own this name color!");
            return;
        }

        // Set the player's display name
        String newDisplayName = selectedColor + player.getName();
        player.setDisplayName(newDisplayName);
        player.setPlayerListName(newDisplayName);

        // Save to config
        plugin.getConfig().set("namecolors." + player.getUniqueId().toString(), selectedColor.getChar());
        plugin.saveConfig();

        player.sendMessage("§aYour name color has been changed to " + selectedColor + this.getColorName(selectedColor) + "§a!");
        rankIntegration.refreshPlayerFormatting(player);
        player.closeInventory();
    }

    private String getColorName(ChatColor color) {
        switch (color) {
            case RED: return "Red";
            case BLUE: return "Blue";
            case AQUA: return "Aqua";
            case DARK_PURPLE: return "Purple";
            case LIGHT_PURPLE: return "Pink";
            case GOLD: return "Gold";
            case GREEN: return "Lime";
            default: return color.name();
        }
    }

    private ChatColor getColorFromSlot(int slot) {
        switch (slot) {
            case 10: return ChatColor.RED;
            case 11: return ChatColor.BLUE;
            case 12: return ChatColor.AQUA;
            case 13: return ChatColor.DARK_PURPLE;
            case 14: return ChatColor.LIGHT_PURPLE; // Pink
            case 15: return ChatColor.GOLD;
            case 16: return ChatColor.GREEN; // Lime
            default: return null;
        }
    }
}