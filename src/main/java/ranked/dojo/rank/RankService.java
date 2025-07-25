package ranked.dojo.rank;

import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.var;
import ranked.dojo.Dojo;
import ranked.dojo.config.ConfigHandler;
import ranked.dojo.util.BukkitUtils;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import ranked.dojo.rank.enums.RankCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Getter
public class RankService {
    private final List<Rank> ranks = new ArrayList<>();
    private final FileConfiguration ranksConfig;

    /**
     * Constructor for the RankRepository class.
     *
     * @param configHandler the config handler
     */
    public RankService(ConfigHandler configHandler) {
        this.ranksConfig = configHandler.getConfig("ranks");
        this.loadRanks();
    }

    /**
     * Load the ranks based on the database type.
     */
    public void loadRanks() {
        if (this.isMongo()) {
            this.ranks.clear();

            var rankCollection = Dojo.getInstance().getDatabaseService().getDatabase().getCollection("ranks");

            var cursor = rankCollection.find();
            if (!cursor.iterator().hasNext()) {
                this.createDefaultRank();
                return;
            }

            for (var document : cursor) {
                Rank rank = documentToRank(document);
                this.ranks.add(rank);
            }
        } else if (this.isFlatFile()) {
            this.ranks.clear();

            if (!this.ranksConfig.contains("ranks")) {
                this.createDefaultRank();
                return;
            }

            for (String rankName : this.ranksConfig.getConfigurationSection("ranks").getKeys(false)) {
                Rank rank = new Rank();
                rank.setName(rankName);
                rank.setPrefix(this.ranksConfig.getString("ranks." + rankName + ".prefix"));
                rank.setSuffix(this.ranksConfig.getString("ranks." + rankName + ".suffix"));
                rank.setWeight(this.ranksConfig.getInt("ranks." + rankName + ".weight"));
                rank.setColor(ChatColor.valueOf(this.ranksConfig.getString("ranks." + rankName + ".color")));
                rank.setBold(this.ranksConfig.getBoolean("ranks." + rankName + ".bold"));
                rank.setItalic(this.ranksConfig.getBoolean("ranks." + rankName + ".italic"));
                rank.setDefaultRank(this.ranksConfig.getBoolean("ranks." + rankName + ".defaultRank"));
                rank.setPermissions(this.ranksConfig.getStringList("ranks." + rankName + ".permissions"));
                String categoryString = this.ranksConfig.getString("ranks." + rankName + ".category");
                if (categoryString != null) {
                    rank.setRankCategory(RankCategory.valueOf(categoryString));
                } else {
                    rank.setRankCategory(RankCategory.DEFAULT);
                }
                this.ranks.add(rank);
            }
        } else {
            BukkitUtils.log("No database type found. Please check your configuration.");
        }
    }

    /**
     * Save the ranks to the database
     */
    public void saveRanks() {
        if (this.isMongo()) {
            var rankCollection = Dojo.getInstance().getDatabaseService().getDatabase().getCollection("ranks");

            rankCollection.deleteMany(new Document());

            for (Rank rank : this.ranks) {
                Document rankDocument = this.rankToDocument(rank);
                rankCollection.replaceOne(new Document("name", rank.getName()), rankDocument, new ReplaceOptions().upsert(true));
            }
        } else if (this.isFlatFile()) {
            this.ranksConfig.set("ranks", null);

            for (Rank rank : this.ranks) {
                this.ranksConfig.set("ranks." + rank.getName() + ".prefix", rank.getPrefix());
                this.ranksConfig.set("ranks." + rank.getName() + ".suffix", rank.getSuffix());
                this.ranksConfig.set("ranks." + rank.getName() + ".weight", rank.getWeight());
                this.ranksConfig.set("ranks." + rank.getName() + ".color", rank.getColor().name());
                this.ranksConfig.set("ranks." + rank.getName() + ".bold", rank.isBold());
                this.ranksConfig.set("ranks." + rank.getName() + ".italic", rank.isItalic());
                this.ranksConfig.set("ranks." + rank.getName() + ".defaultRank", rank.isDefaultRank());
                this.ranksConfig.set("ranks." + rank.getName() + ".permissions", rank.getPermissions());
                this.ranksConfig.set("ranks." + rank.getName() + ".category", rank.getRankCategory() != null ? rank.getRankCategory().name() : RankCategory.DEFAULT.name());
            }

            Dojo.getInstance().getConfigHandler().saveConfig(Dojo.getInstance().getConfigHandler().getConfigFile("ranks"), this.ranksConfig);
        } else {
            BukkitUtils.log("No database type found. Please check your configuration.");
        }
    }

    /**
     * Save a rank to the database
     *
     * @param rank the rank to save
     */
    public void saveRank(Rank rank) {
        if (this.isMongo()) {
            var rankCollection = Dojo.getInstance().getDatabaseService().getDatabase().getCollection("ranks");

            Document rankDocument = this.rankToDocument(rank);
            rankCollection.replaceOne(new Document("name", rank.getName()), rankDocument);
        } else if (this.isFlatFile()) {
            this.ranksConfig.set("ranks." + rank.getName() + ".prefix", rank.getPrefix());
            this.ranksConfig.set("ranks." + rank.getName() + ".suffix", rank.getSuffix());
            this.ranksConfig.set("ranks." + rank.getName() + ".weight", rank.getWeight());
            this.ranksConfig.set("ranks." + rank.getName() + ".color", rank.getColor().name());
            this.ranksConfig.set("ranks." + rank.getName() + ".bold", rank.isBold());
            this.ranksConfig.set("ranks." + rank.getName() + ".italic", rank.isItalic());
            this.ranksConfig.set("ranks." + rank.getName() + ".defaultRank", rank.isDefaultRank());
            this.ranksConfig.set("ranks." + rank.getName() + ".permissions", rank.getPermissions());
            this.ranksConfig.set("ranks." + rank.getName() + ".category", rank.getRankCategory() != null ? rank.getRankCategory().name() : RankCategory.DEFAULT.name());

            Dojo.getInstance().getConfigHandler().saveConfig(Dojo.getInstance().getConfigHandler().getConfigFile("ranks"), this.ranksConfig);
        } else {
            BukkitUtils.log("No database type found. Please check your configuration.");
        }
    }

    /**
     * Convert a Rank object to a Document
     */
    private Document rankToDocument(Rank rank) {
        Document rankDocument = new Document();
        rankDocument.put("name", rank.getName());
        rankDocument.put("prefix", rank.getPrefix());
        rankDocument.put("suffix", rank.getSuffix());
        rankDocument.put("weight", rank.getWeight());
        rankDocument.put("color", rank.getColor().name());
        rankDocument.put("bold", rank.isBold());
        rankDocument.put("italic", rank.isItalic());
        rankDocument.put("defaultRank", rank.isDefaultRank());
        rankDocument.put("permissions", rank.getPermissions());
        rankDocument.put("category", rank.getRankCategory() != null ? rank.getRankCategory().name() : RankCategory.DEFAULT.name());
        return rankDocument;
    }

    /**
     * Convert a Document to a Rank object
     */
    @SuppressWarnings("unchecked")
    private Rank documentToRank(Document document) {
        Rank rank = new Rank();
        rank.setName(document.getString("name"));
        rank.setPrefix(document.getString("prefix"));
        rank.setSuffix(document.getString("suffix"));
        rank.setWeight(document.getInteger("weight"));
        rank.setColor(ChatColor.valueOf(document.getString("color")));
        rank.setBold(document.getBoolean("bold"));
        rank.setItalic(document.getBoolean("italic"));
        rank.setDefaultRank(document.getBoolean("defaultRank"));
        rank.setPermissions((List<String>) document.get("permissions"));
        String categoryString = document.getString("category");
        if (categoryString != null) {
            rank.setRankCategory(RankCategory.valueOf(categoryString));
        } else {
            rank.setRankCategory(RankCategory.DEFAULT);
        }
        return rank;
    }

    /**
     * Create the default rank
     */
    public void createDefaultRank() {
        if (this.isMongo()) {
            for (Rank rank : this.ranks) {
                if (rank.isDefaultRank()) {
                    BukkitUtils.log(rank.getName() + " has defaultRank set as true. Not creating the default rank.");
                    return;
                }
            }

            Rank rank = new Rank();
            rank.setName("Default");
            rank.setPrefix("");
            rank.setSuffix("");
            rank.setWeight(0);
            rank.setColor(ChatColor.GREEN);
            rank.setBold(false);
            rank.setItalic(false);
            rank.setDefaultRank(true);
            rank.setPermissions(Arrays.asList("example.permission", "example.permission2"));
            rank.setRankCategory(RankCategory.DEFAULT);

            this.ranks.add(rank);
        } else if (this.isFlatFile()) {
            if (this.ranksConfig.contains("ranks")) {
                for (String rankName : this.ranksConfig.getConfigurationSection("ranks").getKeys(false)) {
                    if (this.ranksConfig.getBoolean("ranks." + rankName + ".defaultRank")) {
                        BukkitUtils.log(rankName + " has defaultRank set as true. Not creating the default rank.");
                        return;
                    }
                }
            }

            Rank rank = new Rank();
            rank.setName("Default");
            rank.setPrefix("");
            rank.setSuffix("");
            rank.setWeight(0);
            rank.setColor(ChatColor.GREEN);
            rank.setBold(false);
            rank.setItalic(false);
            rank.setDefaultRank(true);
            rank.setPermissions(Arrays.asList("example.permission", "example.permission2"));
            rank.setRankCategory(RankCategory.DEFAULT);

            this.ranks.add(rank);
        } else {
            BukkitUtils.log("No database type found. Please check your configuration.");
        }

        this.saveRanks();
    }

    /**
     * Get a rank by name
     *
     * @param name the name of the rank
     * @return the rank
     */
    public Rank getRank(String name) {
        return this.ranks.stream()
                .filter(rank -> rank.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the default rank
     *
     * @return the default rank
     */
    public Rank getDefaultRank() {
        for (Rank rank : this.ranks) {
            if (rank.isDefaultRank()) {
                return rank;
            }
        }

        return null;
    }

    /**
     * Creates a new rank with the specified name.
     *
     * @param name the name of the rank
     * @param doPrefix whether to add a prefix to the rank
     */
    public void createRank(String name, boolean doPrefix) {
        Rank rank = new Rank();

        rank.setName(name);

        if (doPrefix) {
            rank.setPrefix("&7[&4" + name + "&7] ");
        } else {
            rank.setPrefix("");
        }

        rank.setSuffix("");
        rank.setWeight(0);
        rank.setColor(ChatColor.GREEN);
        rank.setBold(false);
        rank.setItalic(false);
        rank.setDefaultRank(false);
        rank.setPermissions(new ArrayList<>());

        this.ranks.add(rank);
        this.saveRank(rank);
    }

    /**
     * Deletes a rank.
     *
     * @param rank the rank to delete
     */
    public void deleteRank(Rank rank) {
        if (this.isMongo()) {
            this.ranks.remove(rank);
            this.saveRanks();
        } else if (this.isFlatFile()) {
            this.ranksConfig.set("ranks." + rank.getName(), null);
            Dojo.getInstance().getConfigHandler().saveConfig(Dojo.getInstance().getConfigHandler().getConfigFile("ranks"), this.ranksConfig);
        } else {
            BukkitUtils.log("No database type found. Please check your configuration.");
        }
    }

    /**
     * Check if the database is MongoDB
     *
     * @return whether the database is MongoDB
     */
    private boolean isMongo() {
        return Dojo.getInstance().getDatabaseService().isMongo();
    }

    /**
     * Check if the database is flat file
     *
     * @return whether the database is flat file
     */
    private boolean isFlatFile() {
        return Dojo.getInstance().getDatabaseService().isFlatFile();
    }
}