package ranked.dojo.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import ranked.dojo.Dojo;
import ranked.dojo.util.CC;
import ranked.dojo.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Getter
public class DatabaseService {
    private MongoDatabase database;
    private MongoClient mongoClient;

    /**
     * Constructor for the DatabaseService class
     */
    public DatabaseService() {
        if (this.isFlatFile()) {
            BukkitUtils.log("The storage type is set to flat file.");
        } else if (this.isMongo()) {
            this.startMongo();
        } else {
            Bukkit.getPluginManager().disablePlugin(Dojo.getInstance());
        }
    }

    /**
     * Start the MongoDB connection
     */
    public void startMongo() {
        try {
            FileConfiguration config = Dojo.getInstance().getConfig();

            String databaseName = config.getString("mongo.database");
            Bukkit.getConsoleSender().sendMessage(CC.translate("&6Connecting to the MongoDB database..."));

            ConnectionString connectionString = new ConnectionString(Objects.requireNonNull(config.getString("mongo.uri")));
            MongoClientSettings.Builder settings = MongoClientSettings.builder();
            settings.applyConnectionString(connectionString);
            settings.applyToConnectionPoolSettings(builder -> builder.maxConnectionIdleTime(30, TimeUnit.SECONDS));
            settings.retryWrites(true);

            this.mongoClient = MongoClients.create(settings.build());
            this.database = this.mongoClient.getDatabase(databaseName);

            Bukkit.getConsoleSender().sendMessage(CC.translate("&aSuccessfully connected to the MongoDB database."));
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cFailed to connect to the MongoDB database."));
            Bukkit.getPluginManager().disablePlugin(Dojo.getInstance());
        }
    }

    /**
     * Check if the database type is MongoDB
     *
     * @return true if the database type is MongoDB
     */
    public boolean isMongo() {
        return Dojo.getInstance().getConfig().getString("storage-type").equalsIgnoreCase("MONGO");
    }

    /**
     * Check if the database type is flat file
     *
     * @return true if the database type is flat file
     */
    public boolean isFlatFile() {
        return Dojo.getInstance().getConfig().getString("storage-type").equalsIgnoreCase("FLAT_FILE");
    }
}