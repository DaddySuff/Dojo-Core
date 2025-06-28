package ranked.dojo.grant;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ranked.dojo.Dojo;
import ranked.dojo.rank.Rank;


@Data
@Getter
@Setter
public class Grant {
    private String rank;
    private String addedOn;
    private String reason;
    private String addedBy;
    private String removedBy;
    private String removedReason;

    private long addedAt;
    private long removedAt;
    private long duration;

    private boolean active;
    private boolean permanent;

    /**
     * Get the rank object by accessing the rank repository
     *
     * @return the rank object
     */
    public Rank getRank() {
        return Dojo.getInstance().getRankService().getRank(this.rank);
    }

    /**
     * Check if the grant has expired
     *
     * @return if the grant has expired
     */
    public boolean hasExpired() {
        return !this.permanent && System.currentTimeMillis() >= this.addedAt + this.duration;
    }
}