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
     * Get color, style, and name of the rank.
     *
     * @return the rank color, style, and the rank name
     */
    public String getRankWithColor() {
        String style = "";
        if (this.bold) style += "§l";
        if (this.italic) style += "§o";
        return (this.color != null ? this.color : "") + style + this.name + "§r";
    }
}