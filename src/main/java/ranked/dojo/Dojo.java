package ranked.dojo;

import lombok.Getter;
import org.bukkit.event.Listener;
import ranked.dojo.api.menu.listener.MenuListener;
import ranked.dojo.broadcast.BroadcastTask;
import ranked.dojo.chat.ChatService;
import ranked.dojo.chat.listener.ChatListener;
import ranked.dojo.command.CommandUtility;
import ranked.dojo.config.ConfigHandler;
import ranked.dojo.conversation.ConversationHandler;
import ranked.dojo.database.DatabaseService;
import ranked.dojo.godmode.GodModeRepository;
import ranked.dojo.profile.ProfileRepository;
import ranked.dojo.profile.listener.ProfileListener;
import ranked.dojo.namecolor.menu.NameColorMenu;
import ranked.dojo.namecolor.NameColorListener;
import ranked.dojo.namecolor.RankHookListener;
import ranked.dojo.namecolor.integration.RankIntegration;
import ranked.dojo.punishment.PunishmentHandler;
import ranked.dojo.rank.RankService;
import ranked.dojo.spawn.SpawnHandler;
import ranked.dojo.tag.TagService;
import ranked.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ranked.dojo.util.WeatherListener;


import java.util.Arrays;
import java.util.List;

@Getter
public class Dojo extends JavaPlugin {
    @Getter
    private static Dojo instance;
    private ConfigHandler configHandler;
    private DatabaseService databaseService;
    private RankService rankService;
    private ProfileRepository profileRepository;
    private TagService tagService;
    @Getter
    private ChatService chatService;
    private GodModeRepository godModeRepository;
    private SpawnHandler spawnHandler;
    private ConversationHandler conversationHandler;
    private NameColorMenu nameColorMenu;

    @Override
    public void onEnable() {
        instance = this;

        // Disable weather in all worlds on startup
        for (World world : Bukkit.getWorlds()) {
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(0);
            world.setThunderDuration(0);
        }

        // Register weather listener to prevent weather changes
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new WeatherListener(), this);
        pm.registerEvents(new PunishmentHandler(), this);

        this.registerChannels();
        this.registerCommands();
        this.saveDefaultConfig();
        this.initializeConfigHandler();
        this.setupDatabase();
        this.initializeRepositoriesAndServices();
        this.registerHandlers();
        this.registerEvents();
        this.runTasks();

        // Initialize RankIntegration
        RankIntegration rankIntegration = new RankIntegration(this);

        // Initialize and store NameColorMenu
        this.nameColorMenu = new NameColorMenu(this, rankIntegration);
        getServer().getPluginManager().registerEvents(this.nameColorMenu, this);
        getServer().getPluginManager().registerEvents(new NameColorListener(this, rankIntegration), this);
        getServer().getPluginManager().registerEvents(new RankHookListener(this, rankIntegration), this);

        CC.sendEnableMessage();
    }

    @Override
    public void onDisable() {
        if (this.profileRepository.getIProfile() != null) {
            this.profileRepository.saveProfiles();
        }

        CC.sendDisableMessage();

    }

    private void registerChannels() {
        this.measureRuntime("register", "BungeeChannel", () -> this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord"));
    }

    private void initializeConfigHandler() {
        this.measureRuntime("initialize", "ConfigHandler", () -> this.configHandler = new ConfigHandler());
    }

    private void registerCommands() {
        this.measureRuntime("register", "Commands", CommandUtility::registerCommands);
    }

    private void setupDatabase() {
        this.measureRuntime("setup", "DatabaseService", () -> this.databaseService = new DatabaseService());
    }

    private void initializeRepositoriesAndServices() {
        this.measureRuntime("load", "RankService", () -> this.rankService = new RankService(this.configHandler));
        this.measureRuntime("load", "TagService", () -> this.tagService = new TagService(this.configHandler));
        this.measureRuntime("initialize", "ProfileRepository", () -> this.profileRepository = new ProfileRepository(this.databaseService));
        this.measureRuntime("load", "ChatService", () -> this.chatService = new ChatService(false));
        this.measureRuntime("initialize", "GodModeRepository", () -> this.godModeRepository = new GodModeRepository());
    }

    private void registerHandlers() {
        this.measureRuntime("load", "SpawnHandler", () -> this.spawnHandler = new SpawnHandler(this.getConfig()));
        this.measureRuntime("load", "ConversationHandler", () -> this.conversationHandler = new ConversationHandler(this.getConfig()));
    }

    private void registerEvents() {
        List<Listener> listeners = Arrays.asList(
                new ProfileListener(),
                new ChatListener(),
                new MenuListener()
        );
        listeners.forEach(event -> Bukkit.getPluginManager().registerEvents(event, this));
    }

    private void runTasks() {
        if (this.getConfig().getBoolean("broadcast.enabled")) {
            this.measureRuntime("run", "BroadcastTask", () -> new BroadcastTask(this.getConfig()).runTaskTimerAsynchronously(this, 20L * this.broadcastInterval(), 20L * this.broadcastInterval()));
        }
    }

    /**
     * Get the exact bukkit version
     *
     * @return the exact bukkit version
     */
    public String getBukkitVersionExact() {
        String version = Bukkit.getServer().getVersion();
        version = version.split("MC: ")[1];
        version = version.split("\\)")[0];
        return version;
    }

    /**
     * Get the broadcast interval from the config
     *
     * @return the broadcast interval
     */
    private int broadcastInterval() {
        return this.getConfig().getInt("broadcast.send-every");
    }

    /**
     * Measure the runtime of a task
     *
     * @param action the action
     * @param task the task to measure
     * @param runnable the runnable to run
     */
    private void measureRuntime(String action, String task, Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long runtime = System.currentTimeMillis() - start;
        Bukkit.getConsoleSender().sendMessage(CC.translate("&7(&6&l" + this.getDescription().getName() + "&7) " + "&c" + task + " &ftook &4" + runtime + "ms &fto " + action + "."));
    }

    private void sendCustomFont() {
        Arrays.stream(ARTEX_ASCII).forEach(line -> Bukkit.getConsoleSender().sendMessage(CC.translate(line)));
    }


    private final String[] ARTEX_ASCII = {
            "",
            "      $$\\                         \n",
            "      $$ |                        \n",
            " $$$$$$$ | $$$$$$\\  $$\\  $$$$$$\\  \n",
            "$$  __$$ |$$  __$$\\ \\__|$$  __$$\\ \n",
            "$$ /  $$ |$$ /  $$ |$$\\ $$ /  $$ |\n",
            "$$ |  $$ |$$ |  $$ |$$ |$$ |  $$ |\n",
            "\\$$$$$$$ |\\$$$$$$  |$$ |\\$$$$$$  |\n",
            " \\_______| \\______/ $$ | \\______/ \n",
            "              $$\\   $$ |          \n",
            "              \\$$$$$$  |          \n",
            "               \\______/           ",
            ""
    };
}