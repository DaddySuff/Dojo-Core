package me.suff.dojo.tag;

import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.var;
import me.suff.dojo.Dojo;
import me.suff.dojo.config.ConfigHandler;
import me.suff.dojo.util.Logger;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class TagService {
    private final List<Tag> tags;
    private final FileConfiguration tagsConfig;
    private final Map<UUID, String> activeTags = new ConcurrentHashMap<>();

    public TagService(ConfigHandler configHandler) {
        this.tags = new ArrayList<>();
        this.tagsConfig = configHandler.getConfig("tags");
        this.loadTags();
        this.loadActiveTags();
    }

    public Tag getActiveTag(UUID playerId) {
        String tagName = activeTags.get(playerId);
        return tagName != null ? getTag(tagName) : null;
    }

    public String getActiveTagDisplay(UUID playerId) {
        Tag tag = getActiveTag(playerId);
        return tag != null ? " " + tag.getNiceName() : "";
    }

    public boolean setActiveTag(UUID playerId, String tagName) {
        if (getTag(tagName) != null) {
            activeTags.put(playerId, tagName);
            saveActiveTags();
            return true;
        }
        return false;
    }

    public void clearActiveTag(UUID playerId) {
        activeTags.remove(playerId);
        saveActiveTags();
    }

    public boolean hasTag(UUID playerId, String tagName) {
        return tagName.equals(activeTags.get(playerId));
    }

    private void loadActiveTags() {
        if (this.isMongo()) {
            var collection = Dojo.getInstance().getDatabaseService().getDatabase().getCollection("active_tags");
            for (Document doc : collection.find()) {
                activeTags.put(
                        UUID.fromString(doc.getString("playerId")),
                        doc.getString("tagName")
                );
            }
        } else if (this.isFlatFile()) {
            if (tagsConfig.contains("active_tags")) {
                tagsConfig.getConfigurationSection("active_tags").getKeys(false).forEach(uuid -> {
                    activeTags.put(
                            UUID.fromString(uuid),
                            tagsConfig.getString("active_tags." + uuid)
                    );
                });
            }
        }
    }

    private void saveActiveTags() {
        if (this.isMongo()) {
            var collection = Dojo.getInstance().getDatabaseService().getDatabase().getCollection("active_tags");
            collection.deleteMany(new Document());

            activeTags.forEach((playerId, tagName) -> {
                collection.insertOne(new Document()
                        .append("playerId", playerId.toString())
                        .append("tagName", tagName)
                );
            });
        } else if (this.isFlatFile()) {
            tagsConfig.set("active_tags", null);
            activeTags.forEach((playerId, tagName) -> {
                tagsConfig.set("active_tags." + playerId.toString(), tagName);
            });
            Dojo.getInstance().getConfigHandler().saveConfig(
                    Dojo.getInstance().getConfigHandler().getConfigFile("tags"),
                    tagsConfig
            );
        }
    }

    public void loadTags() {
        if (this.isMongo()) {
            tags.clear();
            var tagCollection = Dojo.getInstance().getDatabaseService().getDatabase().getCollection("tags");
            var cursor = tagCollection.find();
            if (!cursor.iterator().hasNext()) return;

            for (var document : cursor) {
                Tag tag = this.documentToTag(document);
                this.tags.add(tag);
            }
        } else if (this.isFlatFile()) {
            if (!this.tags.isEmpty()) {
                this.tags.clear();
            }

            if (this.tagsConfig.getConfigurationSection("tags") == null ||
                    this.tagsConfig.getConfigurationSection("tags").getKeys(false).isEmpty()) {
                Logger.logError("No tags found in the flat file.");
                return;
            }

            this.tagsConfig.getConfigurationSection("tags").getKeys(false).forEach(tagName -> {
                String displayName = tagsConfig.getString("tags." + tagName + ".displayName");
                Material icon = Material.valueOf(tagsConfig.getString("tags." + tagName + ".icon"));
                ChatColor color = ChatColor.valueOf(tagsConfig.getString("tags." + tagName + ".color"));
                int durability = tagsConfig.getInt("tags." + tagName + ".durability");
                boolean bold = tagsConfig.getBoolean("tags." + tagName + ".bold");
                boolean italic = tagsConfig.getBoolean("tags." + tagName + ".italic");

                Tag tag = new Tag(tagName, displayName, icon, color, durability, bold, italic);
                this.tags.add(tag);
            });
        } else {
            Logger.logError("No database type found.");
        }
    }

    public void saveTags() {
        if (this.isMongo()) {
            var tagCollection = Dojo.getInstance().getDatabaseService().getDatabase().getCollection("tags");
            tagCollection.deleteMany(new Document());

            for (Tag tag : this.tags) {
                Document rankDocument = this.tagToDocument(tag);
                tagCollection.replaceOne(new Document("name", tag.getName()), rankDocument, new ReplaceOptions().upsert(true));
            }
        } else if (this.isFlatFile()) {
            if (this.tagsConfig.contains("tags")) {
                this.tagsConfig.set("tags", null);
            }

            for (Tag tag : this.tags) {
                this.tagsConfig.set("tags." + tag.getName() + ".displayName", tag.getDisplayName());
                this.tagsConfig.set("tags." + tag.getName() + ".icon", tag.getIcon().name());
                this.tagsConfig.set("tags." + tag.getName() + ".color", tag.getColor().name());
                this.tagsConfig.set("tags." + tag.getName() + ".durability", tag.getDurability());
                this.tagsConfig.set("tags." + tag.getName() + ".bold", tag.isBold());
                this.tagsConfig.set("tags." + tag.getName() + ".italic", tag.isItalic());
            }

            Dojo.getInstance().getConfigHandler().saveConfig(Dojo.getInstance().getConfigHandler().getConfigFile("tags"), this.tagsConfig);
        } else {
            Logger.logError("No database type found.");
        }
    }

    public void saveTag(Tag tag) {
        if (this.isMongo()) {
            var tagCollection = Dojo.getInstance().getDatabaseService().getDatabase().getCollection("tags");
            Document tagDocument = this.tagToDocument(tag);
            tagCollection.replaceOne(new Document("name", tag.getName()), tagDocument);
        } else if (this.isFlatFile()) {
            this.tagsConfig.set("tags." + tag.getName() + ".displayName", tag.getDisplayName());
            this.tagsConfig.set("tags." + tag.getName() + ".icon", tag.getIcon().name());
            this.tagsConfig.set("tags." + tag.getName() + ".color", tag.getColor().name());
            this.tagsConfig.set("tags." + tag.getName() + ".durability", tag.getDurability());
            this.tagsConfig.set("tags." + tag.getName() + ".bold", tag.isBold());
            this.tagsConfig.set("tags." + tag.getName() + ".italic", tag.isItalic());

            Dojo.getInstance().getConfigHandler().saveConfig(Dojo.getInstance().getConfigHandler().getConfigFile("tags"), this.tagsConfig);
        } else {
            Logger.logError("No database type found.");
        }
    }

    public Document tagToDocument(Tag tag) {
        return new Document("name", tag.getName())
                .append("displayName", tag.getDisplayName())
                .append("icon", tag.getIcon().name())
                .append("color", tag.getColor().name())
                .append("durability", tag.getDurability())
                .append("bold", tag.isBold())
                .append("italic", tag.isItalic());
    }

    private Tag documentToTag(Document document) {
        return new Tag(
                document.getString("name"),
                document.getString("displayName"),
                Material.valueOf(document.getString("icon")),
                ChatColor.valueOf(document.getString("color")),
                document.getInteger("durability"),
                document.getBoolean("bold"),
                document.getBoolean("italic")
        );
    }

    public Tag getTag(String name) {
        return this.tags.stream()
                .filter(tag -> tag.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void createTag(String name, String displayName, Material icon, ChatColor color, int durability, boolean bold, boolean italic) {
        Tag tag = new Tag(name, displayName, icon, color, durability, bold, italic);
        this.tags.add(tag);
        this.saveTag(tag);
    }

    public void deleteTag(Tag tag) {
        this.tags.remove(tag);
        this.saveTags();
    }

    private boolean isMongo() {
        return Dojo.getInstance().getDatabaseService().isMongo();
    }

    private boolean isFlatFile() {
        return Dojo.getInstance().getDatabaseService().isFlatFile();
    }
}