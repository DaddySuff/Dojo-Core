package ranked.dojo.profile.handler.impl;

import ranked.dojo.Dojo;
import ranked.dojo.grant.GrantSerializer;
import ranked.dojo.profile.Profile;
import ranked.dojo.profile.handler.IProfile;
import ranked.dojo.util.BukkitUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.UUID;


public class FlatFileProfileHandler implements IProfile {
    /**
     * Load the profile of a player from the configuration file.
     *
     * @param profile the profile of the player
     */
    public void loadProfile(Profile profile) {
        FileConfiguration config = Dojo.getInstance().getConfigHandler().getConfig("profiles");
        UUID uuid = profile.getUuid();

        if (!config.contains(uuid.toString())) {
            BukkitUtils.log("Profile for " + profile.getUsername() + " not found.");
            return;
        }

        profile.setUsername(config.getString(uuid + ".username"));
        if (config.contains(uuid + ".tag")) {
            String tagName = config.getString(uuid + ".tag");
            Dojo.getInstance().getTagService().setActiveTag(uuid, tagName);
        }
        profile.setRank(Dojo.getInstance().getRankService().getRank(config.getString(uuid + ".rank")));

        List<String> grants = config.getStringList(uuid + ".grants");
        profile.setGrants(GrantSerializer.deserialize(grants));
    }

    /**
     * Save the profile of a player to the configuration file.
     *
     * @param profile the profile of the player
     */
    public void saveProfile(Profile profile) {
        FileConfiguration config = Dojo.getInstance().getConfigHandler().getConfig("profiles");
        UUID uuid = profile.getUuid();

        config.set(uuid + ".username", profile.getUsername());
        String activeTagName = Dojo.getInstance().getTagService().getActiveTag(profile.getUuid()) != null ? Dojo.getInstance().getTagService().getActiveTag(profile.getUuid()).getName() : null;
        if (activeTagName != null) config.set(uuid + ".tag", activeTagName);
        else config.set(uuid + ".tag", null);
        config.set(uuid + ".rank", profile.getHighestRankBasedOnGrant().getName());
        config.set(uuid + ".grants", GrantSerializer.serialize(profile.getGrants()));

        Dojo.getInstance().getConfigHandler().saveConfig(Dojo.getInstance().getConfigHandler().getConfigFile("profiles"), config);
    }
}