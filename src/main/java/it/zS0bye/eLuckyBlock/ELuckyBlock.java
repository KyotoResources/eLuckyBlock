package it.zS0bye.eLuckyBlock;

import it.zS0bye.eLuckyBlock.commands.MainCommand;
import it.zS0bye.eLuckyBlock.files.*;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.listeners.*;
import it.zS0bye.eLuckyBlock.mysql.SQLConnection;
import it.zS0bye.eLuckyBlock.mysql.tables.LuckyTable;
import it.zS0bye.eLuckyBlock.mysql.tables.ScoreTable;
import it.zS0bye.eLuckyBlock.utils.*;
import it.zS0bye.eLuckyBlock.utils.enums.ConsoleUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import me.onlyfire.vandalupdater.UpdateType;
import me.onlyfire.vandalupdater.VandalUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public class ELuckyBlock extends JavaPlugin {

    @Getter
    private static ELuckyBlock instance;

    private FileManager configFile;
    private FileManager languagesFile;
    private FileManager animationsFile;
    private FileManager fireworksFile;
    private SchematicsFile schematics;

    private SQLConnection sqlConnection;
    private LuckyTable luckyTable;
    private ScoreTable scoreTable;

    private HooksManager hooks;

    private final List<String> luckyblocks = new ArrayList<>();

    @Override
    public void onEnable() {

        instance = this;

        this.getLogger().info(ConsoleUtils.RESET + "");
        this.getLogger().info(ConsoleUtils.PURPLE + "             __       __    __    ______  __  ___ ____    ____ " + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "            |  |     |  |  |  |  /      ||  |/  / \\   \\  /   /  " + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "   ,---.    |  |     |  |  |  | |  ,----'|  '  /   \\   \\/   /  " + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "  | .-. :   |  |     |  |  |  | |  |     |    <     \\_    _/   " + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "  \\   --.   |  `----.|  `--'  | |  `----.|  .  \\      |  |     " + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "   `----'   |_______| \\______/   \\______||__|\\__\\     |__|    " + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "       .______    __        ______     ______  __  ___" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "       |   _  \\  |  |      /  __  \\   /      ||  |/  /" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "       |  |_)  | |  |     |  |  |  | |  ,----'|  '  /" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "       |   _  <  |  |     |  |  |  | |  |     |    <" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "       |  |_)  | |  `----.|  `--'  | |  `----.|  .  \\" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "       |______/  |_______| \\______/   \\______||__|\\__\\" + ConsoleUtils.RESET);
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Developed by zS0bye" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Current version v" + this.getDescription().getVersion() + " ● Running on " + this.getServer().getName() + ConsoleUtils.RESET);

        this.loadFiles();
        this.SQLConnection();

        this.loadHooks();

        this.loadCommands();
        this.loadListeners();

        this.loadUpdater();

        LuckyBlock.addRewards(this);
        new Metrics(this, 13289);

        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.PURPLE + "┃ The Plug-in was started successfully ;)" + ConsoleUtils.RESET);
        this.getLogger().info("");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> this.luckyTable.fixLocations(), 30L, 30L);

    }

    @Override
    @SneakyThrows
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(!this.scoreTable.getLuckyScore().containsKey(player.getName())) return;
            this.sqlConnection.saves("UPDATE breaks SET Numbers = ? WHERE PlayerName = ?", this.scoreTable.getScoreMap(player.getName()), player.getName());
        });
    }

    private void SQLConnection() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Connection to database.." + ConsoleUtils.RESET);
            if(Config.DB_TYPE.getString().equalsIgnoreCase("SQLite")) {
                sqlConnection = new SQLConnection(this);
            }else if(Config.DB_TYPE.getString().equalsIgnoreCase("MySQL")) {
                sqlConnection = new SQLConnection();
            }
            this.luckyTable = new LuckyTable(this);
            this.scoreTable = new ScoreTable(this);
        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Connection successful!" + ConsoleUtils.RESET);
    }

    private void loadFiles() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Loading resources.." + ConsoleUtils.RESET);

        this.configFile = new FileManager(this, "config", null).saveDefaultConfig();
        this.languagesFile = new FileManager(this, Config.SETTINGS_LOCALE.getString(), "languages")
                .saveDefaultConfig();
        this.animationsFile = new FileManager(this, "animations", null).saveDefaultConfig();
        this.fireworksFile = new FileManager(this, "fireworks", null).saveDefaultConfig();
        this.schematics = new SchematicsFile(this);

        this.loadLuckyBlocks(false);

        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Resources uploaded successfully!" + ConsoleUtils.RESET);
    }

    public void loadLuckyBlocks(final boolean reload) {
        this.luckyblocks.clear();
        this.luckyblocks.addAll(Arrays.asList("LuckyNormal", "LuckyVIP"));
        this.luckyblocks.addAll(FileManager.getFilesFolder(this, "luckyblocks"));
        this.luckyblocks.forEach(file -> {
            final FileManager luckyblocks = new FileManager(this, file, "luckyblocks");
            if(reload) {
                luckyblocks.reload();
                return;
            }
            luckyblocks.saveDefaultConfig();
        });
    }

    private void loadHooks() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Loading Hooks.." + ConsoleUtils.RESET);

        this.hooks = new HooksManager(this);
        this.hooks.registerPlaceholders();

        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Hooks loaded successfully!" + ConsoleUtils.RESET);
    }

    @SuppressWarnings("all")
    private void loadCommands() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Registering commands.." + ConsoleUtils.RESET);

        this.getCommand("eluckyblock").setExecutor(new MainCommand(this));
        this.getCommand("eluckyblock").setTabCompleter(new MainCommand(this));

        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Commands registered successfully!" + ConsoleUtils.RESET);
    }

    private void loadListeners() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Registering events.." + ConsoleUtils.RESET);

        this.getServer().getPluginManager().registerEvents(new LuckyBlockListener(this), this);
        if(!VersionUtils.legacy()) this.getServer().getPluginManager().registerEvents(new SpongeAbsorbListener(this), this);

        this.getLogger().info(ConsoleUtils.PURPLE + "┃ Events registered successfully!" + ConsoleUtils.RESET);
    }

    private void loadUpdater() {
        if(!Config.CHECK_UPDATE_ENABLED.getBoolean()) return;
        final String resourceId = "97759";
        final UpdateType updateType = UpdateType.valueOf(Config.CHECK_UPDATE_TYPE.getString());
        final VandalUpdater vandalUpdater = new VandalUpdater(this, resourceId, updateType);
        vandalUpdater.setUpdateMessage(Lang.UPDATE_NOTIFICATION.getCustomString());
        vandalUpdater.runTaskTimerAsynchronously(this, 20L, 30 * 60 * 20L);
    }

}
