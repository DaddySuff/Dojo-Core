package me.suff.dojo.api.menu.pagination;

import lombok.Getter;
import me.suff.dojo.api.menu.Button;
import me.suff.dojo.api.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class PaginatedMenu extends Menu {

    private int page = 1;

    {
        setUpdateAfterClick(false);
    }

    // Abstract methods
    public abstract String getPrePaginatedTitle(Player player);
    public abstract Map<Integer, Button> getAllPagesButtons(Player player);

    @Override
    public String getTitle(Player player) {
        return getPrePaginatedTitle(player);
    }

    @Override
    public final Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        // 1. Setup borders
        setupBorders(buttons);

        // 2. Add paginated content
        addPaginatedContent(player, buttons);

        // 3. Add navigation controls
        addNavigationButtons(buttons);

        // 4. Add global buttons
        addGlobalButtons(player, buttons);

        return buttons;
    }

    private void setupBorders(Map<Integer, Button> buttons) {
        Material borderMaterial = getBorderMaterial();
        byte borderColor = getBorderColor();

        // Top and bottom borders
        for (int i = 0; i < 9; i++) {
            buttons.put(i, createGlassButton(borderMaterial, borderColor));
            buttons.put(i + 45, createGlassButton(borderMaterial, borderColor));
        }

        // Side borders
        for (int row = 1; row < 5; row++) {
            buttons.put(row * 9, createGlassButton(borderMaterial, borderColor));
            buttons.put(row * 9 + 8, createGlassButton(borderMaterial, borderColor));
        }
    }

    private void addPaginatedContent(Player player, Map<Integer, Button> buttons) {
        int itemsPerPage = getMaxItemsPerPage();
        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = startIndex + itemsPerPage;

        int contentSlot = 0; // Tracks position in content area
        Map<Integer, Button> allButtons = getAllPagesButtons(player);

        for (Map.Entry<Integer, Button> entry : allButtons.entrySet()) {
            if (entry.getKey() >= startIndex && entry.getKey() < endIndex) {
                int targetSlot = getContentSlot(contentSlot++);
                if (targetSlot != -1) { // -1 means skip slot
                    buttons.put(targetSlot, entry.getValue());
                }
            }
        }
    }

    private int getContentSlot(int contentIndex) {
        // Calculate row and column in content area
        int row = contentIndex / 7; // 7 columns available per row
        int col = contentIndex % 7;

        // Skip if we're beyond inventory capacity
        if (row > 3) return -1;

        // Convert to actual slot number (skip borders)
        return 10 + row * 9 + col; // Starts at slot 10 (second row, second column)
    }

    private void addNavigationButtons(Map<Integer, Button> buttons) {
        buttons.put(45, new PageButton(-1, this)); // Previous page (bottom left)
        buttons.put(53, new PageButton(1, this));  // Next page (bottom right)
    }

    private void addGlobalButtons(Player player, Map<Integer, Button> buttons) {
        Map<Integer, Button> globalButtons = getGlobalButtons(player);
        if (globalButtons != null) {
            buttons.putAll(globalButtons);
        }
    }

    // Configuration methods
    protected Material getBorderMaterial() {
        return Material.valueOf("STAINED_GLASS_PANE");
    }

    protected byte getBorderColor() {
        return 15; // Gray color
    }

    public int getMaxItemsPerPage() {
        return 28; // 4 rows × 7 columns
    }

    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    // Navigation controls
    public final void modPage(Player player, int mod) {
        page += mod;
        getButtons().clear();
        openMenu(player);
    }

    public final int getPages(Player player) {
        int totalItems = getAllPagesButtons(player).size();
        return Math.max(1, (int) Math.ceil((double) totalItems / getMaxItemsPerPage()));
    }

    private Button createGlassButton(Material material, byte color) {
        return new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemStack(material, 1, color);
            }
        };
    }
}