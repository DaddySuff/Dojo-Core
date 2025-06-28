package ranked.dojo.profile.handler;

import ranked.dojo.profile.Profile;


public interface IProfile {

    /**
     * Load a profile from the database
     *
     * @param profile the profile to load
     */
    void loadProfile(Profile profile);

    /**
     * Save a profile to the database
     *
     * @param profile the profile to save
     */
    void saveProfile(Profile profile);
}