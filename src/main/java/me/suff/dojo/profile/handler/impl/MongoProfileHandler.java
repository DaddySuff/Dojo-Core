package me.suff.dojo.profile.handler.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.suff.dojo.Dojo;
import me.suff.dojo.grant.GrantSerializer;
import me.suff.dojo.profile.Profile;
import me.suff.dojo.profile.handler.IProfile;
import me.suff.dojo.util.Logger;
import org.bson.Document;

/**
 * @author Emmy
 * @project Artex
 * @date 29/09/2024 - 09:58
 */
public class MongoProfileHandler implements IProfile {
    /**
     * Load a profile from the database
     *
     * @param profile the profile to load
     */
    public void loadProfile(Profile profile) {
        Document document = Dojo.getInstance().getProfileRepository().getCollection()
                .find(Filters.eq("uuid", profile.getUuid().toString())).first();

        if (document == null) {
            Logger.logError("Profile not found for " + profile.getUsername() + ".");
            return;
        }

        profile.setUsername(document.getString("name"));
        if (document.getString("tag") != null) {
            String tagName = document.getString("tag");
            Dojo.getInstance().getTagService().setActiveTag(profile.getUuid(), tagName);
        }
        profile.setRank(Dojo.getInstance().getRankService().getRank(document.getString("rank")));
        profile.setGrants(GrantSerializer.deserialize(document.getList("grants", String.class)));
    }

    /**
     * Save a profile to the database
     *
     * @param profile the profile to save
     */
    public void saveProfile(Profile profile) {
        Document document = new Document();
        document.put("uuid", profile.getUuid().toString());
        document.put("name", profile.getUsername());
        String activeTagName = Dojo.getInstance().getTagService().getActiveTag(profile.getUuid()) != null ? Dojo.getInstance().getTagService().getActiveTag(profile.getUuid()).getName() : null;
        if (activeTagName != null) document.put("tag", activeTagName);
        document.put("rank", profile.getHighestRankBasedOnGrant().getName());
        document.put("grants", GrantSerializer.serialize(profile.getGrants()));

        Dojo.getInstance().getProfileRepository().getCollection()
                .replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }
}