package it.zS0bye.eLuckyBlock;

import it.zS0bye.eLuckyBlock.api.ILuckyBlockAPI;
import it.zS0bye.eLuckyBlock.api.LuckyBlockAPI;
import it.zS0bye.eLuckyBlock.checker.VersionChecker;
import it.zS0bye.eLuckyBlock.commands.MainCommand;
import it.zS0bye.eLuckyBlock.files.*;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.files.enums.Lucky;
import it.zS0bye.eLuckyBlock.files.enums.Rewards;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.listeners.*;
import it.zS0bye.eLuckyBlock.mysql.SQLConnection;
import it.zS0bye.eLuckyBlock.mysql.tables.LuckyTable;
import it.zS0bye.eLuckyBlock.mysql.tables.ScoreTable;
import it.zS0bye.eLuckyBlock.rewards.RandomReward;
import it.zS0bye.eLuckyBlock.rewards.ExcReward;
import it.zS0bye.eLuckyBlock.utils.*;
import it.zS0bye.eLuckyBlock.utils.enums.ConsoleUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import me.onlyfire.vandalupdater.UpdateType;
import me.onlyfire.vandalupdater.VandalUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ELuckyBlock extends JavaPlugin {

    private LanguagesFile lang;
    private LuckyBlocksFile lucky;
    private AnimationsFile animations;
    private RewardsFile rewards;
    private FireworksFile fireworks;
    private SchematicsFile schematics;
    private SQLConnection sqlConnection;
    private LuckyTable luckyTable;
    private ScoreTable scoreTable;

    @Getter
    private static ELuckyBlock instance;
    @Getter
    private static ILuckyBlockAPI api;

    private final Map<String, Integer> luckyScore = new HashMap<>();
    private final Map<String, RandomReward<ExcReward>> randomReward = new HashMap<>();

    @Override
    public void onEnable() {

        instance = this;

        getLogger().info(ConsoleUtils.RESET + "");
        getLogger().info(ConsoleUtils.PURPLE + " .,::::::       :::      ...    :::  .,-:::::  :::  .  .-:.     ::-." + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + " ;;;;''''       ;;;      ;;     ;;;,;;;'````'  ;;; .;;,.';;.   ;;;;'" + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + "  [[cccc        [[[     [['     [[[[[[         [[[[[/'    '[[,[[['  " + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + "  $$\"\"\"\"        $$'     $$      $$$$$$        _$$$$,        c$$\" " + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + "  888oo,__     o88oo,.__88    .d888`88bo,__,o,\"888\"88o,   ,8P\"`  " + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + "  \"\"\"\"YUMMM    \"\"\"\"YUMMM \"YmmMMMM\"\"  \"YUMMMMMP\"MMM \"MMP\" mM\"" + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + " :::::::.   :::         ...       .,-:::::  :::  ." + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + " ;;;'';;'  ;;;      .;;;;;;;.  ,;;;'````'  ;;; .;;,." + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + " [[[__[[\\. [[[     ,[[     \\[[,[[[         [[[[[/''" + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + " $$\"\"\"\"Y$$ $$'     $$$,     $$$$$$        _$$$$," + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + " _88o,,od8Po88oo,.__\"888,_ _,88P`88bo,__,o,\"888\"88o," + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + " \"\"YUMMMP\" \"\"\"\"YUMMM  \"YMMMMMP\"   \"YUMMMMMP\"MMM \"MMP\"" + ConsoleUtils.RESET);
        getLogger().info("");
        getLogger().info(ConsoleUtils.PURPLE + "┃ Developed by zS0bye" + ConsoleUtils.RESET);
        getLogger().info(ConsoleUtils.PURPLE + "┃ Current version v" + getDescription().getVersion() + ConsoleUtils.RESET);
        loadFiles();
        SQLConnection();

        getLogger().info("");
        getLogger().info(ConsoleUtils.PURPLE + "┃ Loading Hooks.." + ConsoleUtils.RESET);
        new HooksManager(this);
        getLogger().info(ConsoleUtils.PURPLE + "┃ Hooks loaded successfully!" + ConsoleUtils.RESET);

        loadCommands();
        loadListeners();

        loadUpdater();

        api = new LuckyBlockAPI();
        addRewards();
        new Metrics(this, 13289);

        getLogger().info("");
        getLogger().info(ConsoleUtils.PURPLE + "┃ The Plug-in was started successfully ;)" + ConsoleUtils.RESET);
        getLogger().info("");

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> this.luckyTable.fixLocations(), 30L, 30L);

    }

    @Override
    @SneakyThrows
    public void onDisable() {
        this.sqlConnection.closeConnection();
    }

    private void SQLConnection() {
        getLogger().info("");
        getLogger().info(ConsoleUtils.PURPLE + "┃ Connection to database.." + ConsoleUtils.RESET);
            if(Config.DB_TYPE.getString().equalsIgnoreCase("SQLite")) {
                sqlConnection = new SQLConnection(this);
            }else if(Config.DB_TYPE.getString().equalsIgnoreCase("MySQL")) {
                sqlConnection = new SQLConnection();
            }
            this.luckyTable = new LuckyTable(this);
            this.scoreTable = new ScoreTable(this);
        getLogger().info(ConsoleUtils.PURPLE + "┃ Connection successful!" + ConsoleUtils.RESET);
    }

    private void loadFiles() {
        getLogger().info("");
        getLogger().info(ConsoleUtils.PURPLE + "┃ Loading resources.." + ConsoleUtils.RESET);

        saveDefaultConfig();
        this.lang = new LanguagesFile(this);
        this.lucky = new LuckyBlocksFile(this);
        this.animations = new AnimationsFile(this);
        this.rewards = new RewardsFile(this);
        this.fireworks = new FireworksFile(this);
        this.schematics = new SchematicsFile(this);

        getLogger().info(ConsoleUtils.PURPLE + "┃ Resources uploaded successfully!" + ConsoleUtils.RESET);
    }

    private void loadCommands() {
        getLogger().info("");
        getLogger().info(ConsoleUtils.PURPLE + "┃ Registering commands.." + ConsoleUtils.RESET);

        getCommand("eluckyblock").setExecutor(new MainCommand(this));
        getCommand("eluckyblock").setTabCompleter(new MainCommand(this));

        getLogger().info(ConsoleUtils.PURPLE + "┃ Commands registered successfully!" + ConsoleUtils.RESET);
    }

    private void loadListeners() {
        getLogger().info("");
        getLogger().info(ConsoleUtils.PURPLE + "┃ Registering events.." + ConsoleUtils.RESET);

        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new FireworkListener(), this);

        this.lucky.getConfig().getKeys(false).forEach(luckyblocks -> {
            Bukkit.getPluginManager().registerEvents(new LuckyBlockListener(luckyblocks), this);
            Bukkit.getPluginManager().registerEvents(new UniqueBlockListener(luckyblocks), this);
            Bukkit.getPluginManager().registerEvents(new InstantBlockListener(luckyblocks), this);

            if (!VersionChecker.getV1_8()
                    && !VersionChecker.getV1_9()
                    && !VersionChecker.getV1_10()
                    && !VersionChecker.getV1_11()
                    && !VersionChecker.getV1_12()
                    && !VersionChecker.getV1_13()
                    && !VersionChecker.getV1_14()
                    && !VersionChecker.getV1_15()) {
                Bukkit.getPluginManager().registerEvents(new SpongeListener(luckyblocks), this);
            }
        });

        getLogger().info(ConsoleUtils.PURPLE + "┃ Events registered successfully!" + ConsoleUtils.RESET);
    }

    private void loadUpdater() {
        if(!Config.CHECK_UPDATE_ENABLED.getBoolean()) {
            return;
        }
        String resourceId = "97759";
        UpdateType updateType = UpdateType.valueOf(Config.CHECK_UPDATE_TYPE.getString());
        VandalUpdater vandalUpdater = new VandalUpdater(this, resourceId, updateType);
        vandalUpdater.setUpdateMessage(Lang.UPDATE_NOTIFICATION.getCustomString());
        vandalUpdater.runTaskTimerAsynchronously(this, 20L, 30 * 60 * 20L);
    }

    public void addRewards() {
        Lucky.LUCKYBLOCK.getKeys().forEach(luckyblocks -> {
            if (randomReward.containsKey(luckyblocks))
                return;

            RandomReward<ExcReward> random = new RandomReward<>();

            for(String reward : Rewards.LUCKYBLOCK.getConfigurationSection(luckyblocks))
                random.add(Rewards.CHANCE.getDouble(luckyblocks, "." + reward),
                        new ExcReward(Rewards.EXECUTE.getStringList(luckyblocks, "." + reward)));

            randomReward.put(luckyblocks, random);
        });
    }

}
