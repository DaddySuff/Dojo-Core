package ranked.dojo.profile;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import ranked.dojo.Dojo;
import ranked.dojo.database.DatabaseService;
import ranked.dojo.profile.handler.IProfile;
import ranked.dojo.profile.handler.impl.FlatFileProfileHandler;
import ranked.dojo.profile.handler.impl.MongoProfileHandler;
import ranked.dojo.util.BukkitUtils;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Getter
public class ProfileRepository {
    private final DatabaseService databaseService;
    public MongoCollection<Document> collection;
    private final Map<UUID, Profile> profiles;
    public final IProfile iProfile;

    /**
     * Constructor for the ProfileRepository class
     *
     * @param databaseService the database service
     */
    public ProfileRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
        this.profiles = new HashMap<>();

        this.iProfile = this.determineHandler();
    }

    /**
     * Get a profile by its UUID without adding it to the map if it doesn't exist
     *
     * @param uuid the UUID of the profile
     * @return the profile
     */
    public Profile getProfileWithNoAdding(UUID uuid) {
        return this.profiles.get(uuid);
    }

    /**
     * Get a profile by its UUID and add it to the map if it doesn't exist
     *
     * @param uuid the UUID of the profile
     * @return the profile
     */
    public Profile getProfile(UUID uuid) {
        if (this.profiles.containsKey(uuid)) {
            return this.profiles.get(uuid);
        }

        Profile profile = new Profile(uuid);
        profile.load();

        this.addProfile(uuid, profile);
        return profile;
    }

    /**
     * Add or update a profile by its UUID.
     *
     * @param uuid    the UUID of the profile
     * @param profile the profile to add or update
     */
    public void addProfile(UUID uuid, Profile profile) {
        this.profiles.put(uuid, profile);
    }

    /**
     * Save all profiles to the database.
     */
    public void saveProfiles() {
        this.profiles.values().forEach(Profile::save);
    }

    /**
     * Determine the handler for the profile
     *
     * @return the handler for the profile
     */
    private IProfile determineHandler() {
        IProfile iProfile;
        if (this.databaseService.isMongo()) {
            this.collection = this.databaseService.getDatabase().getCollection("profiles");
            iProfile = new MongoProfileHandler();
        } else if (this.databaseService.isFlatFile()) {
            iProfile = new FlatFileProfileHandler();
        } else {
            BukkitUtils.log("No database type found.");
            Bukkit.getPluginManager().disablePlugin(Dojo.getInstance());
            iProfile = null;
        }
        return iProfile;
    }
}