package ranked.dojo.instance.menu;

import ranked.dojo.api.menu.Button;
import ranked.dojo.api.menu.Menu;
import ranked.dojo.api.menu.button.BuilderButton;
import ranked.dojo.instance.menu.button.ShutDownButton;
import ranked.dojo.locale.Locale;
import ranked.dojo.util.BukkitUtils;
import ranked.dojo.util.DateUtil;
import ranked.dojo.util.ProjectInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class InstanceMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Server Instance (" + Locale.SERVER_NAME.getString() + ")";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        
        buttons.put(11, new BuilderButton("&6&lServer Details", new ItemStack(Material.PAPER), 0,
                Arrays.asList(
                        "",
                        "&6&l● &fServer Name: &e" + Locale.SERVER_NAME.getString(),
                        "&6&l● &fServer Region: &e" + Locale.SERVER_REGION.getString(),
                        "&6&l● &fVersion: &e" + ProjectInfo.BUKKIT_VERSION_EXACT,
                        "&6&l● &fSpigot: &e" + ProjectInfo.SERVER_TYPE,
                        "&6&l● &fServer MOTD:",
                        "&f&l| &r" + String.format(Bukkit.getServer().getMotd()),
                        ""
                )));

        buttons.put(13, new BuilderButton("&6&lServer Status", new ItemStack(Material.BEACON), 0,
                Arrays.asList(
                        "",
                        "&6&l● &fOnline Players: &e" + BukkitUtils.getOnlinePlayers().size(),
                        "&6&l● &fMax Players: &e" + Bukkit.getMaxPlayers(),
                        "&6&l● &fUptime: &e" + DateUtil.formatTimeMillis(System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()),
                        ""
                )));

        buttons.put(31, new ShutDownButton("&4&lShutdown Server", new ItemStack(Material.BARRIER), 0,
                Arrays.asList(
                        "",
                        "&7&oClick to shutdown the server.",
                        ""
                )));

        this.addGlass(buttons, 15);
        
        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}