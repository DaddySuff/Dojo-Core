package ranked.dojo.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class BukkitUtils {
    private static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static String getServerVersion() {
        return SERVER_VERSION;
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        try {
            return Class.forName("net.minecraft.server." + SERVER_VERSION + "." + name);
        } catch (ClassNotFoundException e) {
            log("Failed to find NMS class: " + name + ". Please check the server version.");
            return null;
        }
    }

    public static Class<?> getCraftBukkitClass(String path) throws ClassNotFoundException {
        try {
            return Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + "." + path);
        } catch (ClassNotFoundException e) {
            log("Failed to find CraftBukkit class: " + path + ". Please check the server version.");
            return null;
        }
    }



    public static @NotNull List<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(message));
    }

    public static void stop() {
        Bukkit.shutdown();
    }
}
