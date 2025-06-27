package me.suff.dojo.instance.menu;

import me.suff.dojo.api.menu.Button;
import me.suff.dojo.api.menu.Menu;
import me.suff.dojo.api.menu.button.BuilderButton;
import me.suff.dojo.instance.menu.button.ShutDownButton;
import me.suff.dojo.locale.Locale;
import me.suff.dojo.util.DateUtil;
import me.suff.dojo.util.ProjectInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Artex
 * @date 30/08/2024 - 21:01
 */
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
                        "&6&l● &fSpigot: &e" + ProjectInfo.SPIGOT,
                        "&6&l● &fServer Port: &e" + Bukkit.getServer().getPort(),
                        "&6&l● &fServer MOTD:",
                        "&f&l| &r" + String.format(Bukkit.getServer().getMotd()),
                        ""
                )));

        buttons.put(13, new BuilderButton("&6&lServer Status", new ItemStack(Material.BEACON), 0,
                Arrays.asList(
                        "",
                        "&6&l● &fOnline Players: &e" + Bukkit.getOnlinePlayers().size(),
                        "&6&l● &fMax Players: &e" + Bukkit.getMaxPlayers(),
                        //"&f● Server TPS: &4" + TPSUtil.getTPSStatus(TPSUtil.getRecentTps()[0]),
                        "&6&l● &fUptime: &e" + DateUtil.formatTimeMillis(System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()),
                        ""
                )));

        buttons.put(15, new BuilderButton("&6&lServer Information", new ItemStack(Material.BOOK), 0,
                Arrays.asList(
                        "",
                        "&6&l● &fServer Version: &e" + Bukkit.getVersion(),
                        "&6&l● &fServer OS: &e" + System.getProperty("os.name"),
                        "&6&l● &fServer OS Version: &e" + System.getProperty("os.version"),
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