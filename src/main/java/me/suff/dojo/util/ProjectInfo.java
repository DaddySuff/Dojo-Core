package me.suff.dojo.util;

import lombok.experimental.UtilityClass;
import me.suff.dojo.Dojo;
import org.bukkit.Bukkit;

/**
 * @author Emmy
 * @project Artex
 * @date 01/09/2024 - 11:36
 */
@UtilityClass
public class ProjectInfo {
    private final Dojo plugin = Dojo.getInstance();

    public String NAME = plugin.getDescription().getName();
    public String VERSION = plugin.getDescription().getVersion();
    public String AUTHORS = String.join(", ", plugin.getDescription().getAuthors());
    public String DESCRIPTION = plugin.getDescription().getDescription();

    public String WEBSITE = plugin.getDescription().getWebsite();
    public String DISCORD = "https://discord.gg/prbw";
    public String GITHUB = "https://github.com/hmEmmy/Artex";

    public String BUKKIT_VERSION_EXACT = plugin.getBukkitVersionExact();
    public String SPIGOT = Bukkit.getName();
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
            Class.forName("org.spigotmc.SpigotConfig");
            return "Spigot";
        } catch (ClassNotFoundException ignored) {}
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return "PaperSpigot";
        } catch (ClassNotFoundException ignored) {}
        return Bukkit.getName();
    }
}