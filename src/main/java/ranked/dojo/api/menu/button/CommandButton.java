package ranked.dojo.api.menu.button;

import lombok.AllArgsConstructor;
import ranked.dojo.api.menu.Button;
import ranked.dojo.util.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;


@AllArgsConstructor
public class CommandButton extends Button {

    private String command;
    private ItemStack itemStack;
    private List<String> lore;


    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(itemStack)
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) {
            return;
        }

        player.performCommand(command);
    }
}
