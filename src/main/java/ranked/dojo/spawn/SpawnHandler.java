package ranked.dojo.spawn;

import lombok.Getter;
import ranked.dojo.Dojo;
import ranked.dojo.util.LocationUtil;
import ranked.dojo.util.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


@Getter
public class SpawnHandler {
    private Location location;
    private final FileConfiguration config;

    /**
     * Constructor for the SpawnHandler class.
     *
     * @param config the config file
     */
    public SpawnHandler(FileConfiguration config) {
        this.config = config;
        this.loadLocation();
    }

    /**
     * Load the spawn location from the config file
     */
    public void loadLocation() {
        Location location = LocationUtil.deserialize(this.config.getString("spawn.join-location"));
        if (location == null) {
            BukkitUtils.log("Spawn location is null.");
            return;
        }

        this.location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Set the spawn location and save it to the config
     *
     * @param location the location to set
     */
    public void saveLocation(Location location) {
        this.location = location;
        this.config.set("spawn.join-location", LocationUtil.serialize(location));
        Dojo.getInstance().saveConfig();
    }

    /**
     * Teleport a player to the spawn location
     *
     * @param player the player to teleport
     */
    public void teleportToSpawn(Player player) {
        Location location = this.location;
        if (location == null) {
            BukkitUtils.log("Spawn location is null.");
            return;
        }

        player.teleport(location);
    }
}