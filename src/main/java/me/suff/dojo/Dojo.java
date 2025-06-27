package me.suff.dojo;

import lombok.Getter;
import me.suff.dojo.api.command.CommandFramework;
import me.suff.dojo.api.menu.listener.MenuListener;
import me.suff.dojo.broadcast.BroadcastTask;
import me.suff.dojo.chat.ChatService;
import me.suff.dojo.chat.listener.ChatListener;
import me.suff.dojo.command.CommandUtility;
import me.suff.dojo.config.ConfigHandler;
import me.suff.dojo.conversation.ConversationHandler;
import me.suff.dojo.database.DatabaseService;
import me.suff.dojo.godmode.GodModeRepository;
import me.suff.dojo.profile.ProfileRepository;
import me.suff.dojo.profile.listener.ProfileListener;
import me.suff.dojo.namecolor.command.NameColorCommand;
import me.suff.dojo.namecolor.menu.NameColorMenu;
import me.suff.dojo.namecolor.NameColorListener;
import me.suff.dojo.namecolor.RankHookListener;
import me.suff.dojo.namecolor.integration.RankIntegration;
import me.suff.dojo.rank.RankService;
import me.suff.dojo.spawn.SpawnHandler;
import me.suff.dojo.tag.TagService;
import me.suff.dojo.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * |------------------------------------------------------------------------------------------------------------------------------------------------|
 *   This project is licensed under Apache License 2.0, see LICENSE.
 *   Copying, modifying or distributing this project is allowed as long as credits maintain.
 *   Credentials are these file headers, the plugin.yml and anything else in relation with the author's name, including the LICENSE file.
 *   Basically, if you do not follow the usage, you're stealing this project, aka. skidding it, which is not cool :|
 * |------------------------------------------------------------------------------------------------------------------------------------------------|
 *
 * @author Emmy
 * @project Artex
 * @date 15/08/2024 - 20:11
 */
@Getter
public class Dojo extends JavaPlugin {

    @Getter
    private static Dojo instance;

    private ConfigHandler configHandler;
    private CommandFramework commandFramework;
    private DatabaseService databaseService;
    private RankService rankService;
    private ProfileRepository profileRepository;
    private TagService tagService;
    private ChatService chatService;
    private GodModeRepository godModeRepository;
    private SpawnHandler spawnHandler;
    private ConversationHandler conversationHandler;
    private NameColorMenu nameColorMenu;

    @Override
    public void onEnable() {
        instance = this;

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
        this.measureRuntime("initialize", "CommandFramework", () -> this.commandFramework = new CommandFramework());
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

    public NameColorMenu getNameColorMenu() {
        return this.nameColorMenu;
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