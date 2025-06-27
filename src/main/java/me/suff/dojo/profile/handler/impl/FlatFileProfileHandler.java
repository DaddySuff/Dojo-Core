package me.suff.dojo.profile.handler.impl;

import me.suff.dojo.Dojo;
import me.suff.dojo.grant.GrantSerializer;
import me.suff.dojo.profile.Profile;
import me.suff.dojo.profile.handler.IProfile;
import me.suff.dojo.util.Logger;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Artex
 * @date 23/10/2024 - 12:18
 */
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
            Logger.logError("Profile for " + profile.getUsername() + " not found.");
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