package ranked.dojo.profile;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import ranked.dojo.Dojo;
import ranked.dojo.grant.Grant;
import ranked.dojo.locale.Locale;
import ranked.dojo.permissible.DojoPermissible;
import ranked.dojo.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
public class Profile {
    private final UUID uuid;
    private String username;
    private String tag;
    private Rank rank;
    private List<Grant> grants;

    /**
     * Constructor for the Profile class.
     *
     * @param uuid the UUID of the profile
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.username = Bukkit.getOfflinePlayer(this.uuid).getName();
        this.tag = "";
        this.rank = Dojo.getInstance().getRankService().getDefaultRank();
        this.grants = new ArrayList<>();
    }

    public void load() {
        Dojo.getInstance().getProfileRepository().getIProfile().loadProfile(this);
    }

    public void save() {
        Dojo.getInstance().getProfileRepository().getIProfile().saveProfile(this);
    }

    /**
     * Get the highest rank based on the grants of the player.
     * Filter by active grants and their rank weight.
     *
     * @return the highest rank based on the grants of the player or else the default rank.
     */
    public Rank getHighestRankBasedOnGrant() {
        return this.getGrants().stream()
                .filter(grant -> grant.isActive() && grant.getRank() != null)
                .max(Comparator.comparingInt(grant -> grant.getRank().getWeight()))
                .map(Grant::getRank)
                .orElse(Dojo.getInstance().getRankService().getDefaultRank());
    }

    /**
     * Check if the player has the default grant.
     *
     * @return true if the player has the default grant, false otherwise
     */
    public boolean hasDefaultGrant() {
        return Dojo.getInstance().getProfileRepository().getProfileWithNoAdding(this.uuid).getGrants()
                .stream()
                .anyMatch(grant -> grant.getRank().isDefaultRank());
    }


    /**
     * Usually when an admin executes /rank delete, the rank of the grant still remains (theoretically).
     * But the rank is null, so it causes errors upon loading profile because we call getHighestRankBasedOnGrant().
     * Therefore, we remove the grant whose rank is null.
     */
    public void removeGrantWithNullRank() {
        this.grants.removeIf(grant -> grant.getRank() == null);
    }

    /**
     * Get all ranks from the grants and return their permissions.
     *
     * @return a list of permissions
     */
    private List<Permission> getRankPermissionsBasedOnGrant() {
        List<Permission> permissions = new ArrayList<>();
        this.grants.forEach(grant -> {
            if (grant.getRank() != null || grant.getRank().getPermissions() != null) {
                grant.getRank().getPermissions().forEach(permission -> permissions.add(new Permission(permission)));
            }
        });
        return permissions;
    }

    /**
     * Get all permissions from the player's active grants' ranks.
     *
     * @return a set of permission strings
     */
    public Set<String> getAllRankPermissions() {
        Set<String> permissions = new HashSet<>();
        for (Grant grant : this.grants) {
            if (grant.isActive() && grant.getRank() != null && grant.getRank().getPermissions() != null) {
                permissions.addAll(grant.getRank().getPermissions());
            }
        }
        return permissions;
    }

    /**
     * Add the default grant to the player.
     */
    private void addFirstDefaultGrant() {
        Grant grant = new Grant();
        grant.setRank(Dojo.getInstance().getRankService().getDefaultRank().getName());
        grant.setPermanent(true);
        grant.setDuration(0);
        grant.setReason("Default rank");
        grant.setAddedBy("Console");
        grant.setAddedAt(System.currentTimeMillis());
        grant.setAddedOn(Locale.SERVER_NAME.getString());
        grant.setActive(true);

        Dojo.getInstance().getProfileRepository().getProfile(this.uuid).setRank(Dojo.getInstance().getRankService().getDefaultRank());

        this.grants.add(grant);
        this.save();
    }

    /**
     * Determine the player's rank and attach its permissions using DojoPermissible.
     */
    public void determineRankAndAttachPerms() {
        if (!this.hasDefaultGrant()) {
            this.addFirstDefaultGrant();
        }

        Rank highestGrant = this.getHighestRankBasedOnGrant();
        this.setRank(highestGrant);

        // Inject permissions into DojoPermissible
        org.bukkit.entity.Player player = org.bukkit.Bukkit.getPlayer(this.uuid);
        if (player != null && player.isOnline()) {
            org.bukkit.permissions.Permissible permissible = player instanceof org.bukkit.permissions.Permissible ? (org.bukkit.permissions.Permissible) player : null;
            if (permissible instanceof ranked.dojo.permissible.DojoPermissible) {
                ranked.dojo.permissible.DojoPermissible dojoPermissible = (ranked.dojo.permissible.DojoPermissible) permissible;
                dojoPermissible.clearRawPermissions();
                for (String perm : this.getAllRankPermissions()) {
                    dojoPermissible.addRawPermission(perm, true);
                }
                dojoPermissible.recalculatePermissions();
            }
        }
    }
}
