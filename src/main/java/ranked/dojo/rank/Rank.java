package ranked.dojo.rank;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import ranked.dojo.rank.enums.RankCategory;

import java.util.List;


@Getter
@Setter
public class Rank {
    private String name;
    private String prefix;
    private String suffix;

    private int weight;

    private ChatColor color;

    private boolean bold;
    private boolean italic;
    private boolean defaultRank;

    private List<String> permissions;

    private RankCategory rankCategory;

    /**
     * Get color and name of the rank.
     *
     * @return the rank color and the rank name
     */
    public String getRankWithColor() {
        return this.color + this.name;
    }
}