package ranked.dojo.util;

import lombok.experimental.UtilityClass;
import ranked.dojo.Dojo;
import org.bukkit.Bukkit;


@UtilityClass
public class ProjectInfo {
    private final Dojo plugin = Dojo.getInstance();

    public String NAME = plugin.getDescription().getName();
    public String VERSION = plugin.getDescription().getVersion();
    public String AUTHORS = String.join(", ", plugin.getDescription().getAuthors());
    public String DESCRIPTION = plugin.getDescription().getDescription();

    public String BUKKIT_VERSION_EXACT = plugin.getBukkitVersionExact();
    public String SERVER_TYPE = detectServerType();

    private String detectServerType() {
        try {
            Class.forName("net.curxxed.dev.WinterSpigot");
            return "WinterSpigot";
        } catch (ClassNotFoundException ignored) {}
        try {
            Class.forName("me.scalebound.pspigot.PSpigotConfig");
            return "pSpigot";
        } catch (ClassNotFoundException ignored) {}
        try {
            Class.forName("club.minemen.spigot.ClubSpigot");
            return "ClubSpigot";
        } catch (ClassNotFoundException ignored) {}
        try {
            Class.forName("xyz.swift.spigot.FulfillSpigot");
            return "FulfillSpigot";
        } catch (ClassNotFoundException ignored) {}
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return "PaperSpigot";
        } catch (ClassNotFoundException ignored) {}
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            return "Spigot";
        } catch (ClassNotFoundException ignored) {}
        return Bukkit.getName();
    }
}