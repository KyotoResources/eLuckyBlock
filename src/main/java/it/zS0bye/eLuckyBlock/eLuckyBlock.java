package it.zS0bye.eLuckyBlock;

import it.zS0bye.eLuckyBlock.commands.MainCommand;
import it.zS0bye.eLuckyBlock.database.SQLConnection;
import it.zS0bye.eLuckyBlock.database.SQLLuckyBlock;
import it.zS0bye.eLuckyBlock.files.*;
import it.zS0bye.eLuckyBlock.hooks.PlaceholderAPI;
import it.zS0bye.eLuckyBlock.listeners.FireworkListener;
import it.zS0bye.eLuckyBlock.listeners.JoinListener;
import it.zS0bye.eLuckyBlock.listeners.LuckyBlockListener;
import it.zS0bye.eLuckyBlock.listeners.SpongeListener;
import it.zS0bye.eLuckyBlock.updater.UpdateType;
import it.zS0bye.eLuckyBlock.updater.VandalUpdater;
import it.zS0bye.eLuckyBlock.utils.*;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class eLuckyBlock extends JavaPlugin {

    private LanguagesFile lang;
    private LuckyBlocksFile lucky;
    private AnimationsFile animations;
    private RewardsFile rewards;
    private FireworksFile fireworks;
    private SQLConnection sqlConnection;
    private SQLLuckyBlock sqlLuckyBlock;
    private Economy economy;
    @Getter
    private static eLuckyBlock instance;

    private final Map<Player, BukkitTask> titleTask = new HashMap<>();
    private final Map<Player, Integer> titleTicks = new HashMap<>();
    private final Map<Player, BukkitTask> actionTask = new HashMap<>();
    private final Map<Player, Integer> actionTicks = new HashMap<>();
    private final Map<Player, BukkitTask> bossBarTask = new HashMap<>();
    private final Map<Player, Integer> bossBarTicks = new HashMap<>();
    private final Map<Player, BukkitTask> bossTimesTask = new HashMap<>();
    private final Map<Player, BossBar> bossTimes = new HashMap<>();
    private final Map<Player, Integer> fireworkTicks = new HashMap<>();
    private final Map<Player, BukkitTask> fireworkTask = new HashMap<>();
    private final Map<String, Integer> luckyBreaks = new HashMap<>();

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
        getLogger().info("");
        getLogger().info(ConsoleUtils.PURPLE + "┃ Loading resources.." + ConsoleUtils.RESET);

        loadFiles();

        SQLConnection();

        loadHooks();

        loadCommands();
        loadListeners();

        loadUpdater();

        new Metrics(this, 13289);

        getLogger().info(ConsoleUtils.PURPLE + "┃ Resources uploaded successfully!" + ConsoleUtils.RESET);
        getLogger().info("");
        getLogger().info(ConsoleUtils.PURPLE + "┃ The Plug-in was started successfully ;)" + ConsoleUtils.RESET);
        getLogger().info("");

    }

    @Override
    public void onDisable() {

        try {
            this.sqlConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void SQLConnection() {
        try {
            if(ConfigUtils.DB_TYPE.getString().equalsIgnoreCase("SQLite")) {
                sqlConnection = new SQLConnection(this);
            }else if(ConfigUtils.DB_TYPE.getString().equalsIgnoreCase("MySQL")) {
                String replace = ConfigUtils.DB_CUSTOMURI.getString().
                        replace("%host%", ConfigUtils.DB_HOST.getString()).
                        replace("%port%", ConfigUtils.DB_PORT.getString()).
                        replace("%database%", ConfigUtils.DB_NAME.getString());
                sqlConnection = new SQLConnection(this, replace, ConfigUtils.DB_USER.getString(), ConfigUtils.DB_PASSWORD.getString());
            }
            this.sqlLuckyBlock = new SQLLuckyBlock(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHooks() {
        if(ConfigUtils.HOOKS_PLACEHOLDERAPI.getBoolean()) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                new PlaceholderAPI(this).register();
            }else {
                getLogger().severe("Could not find PlaceholderAPI! This plugin is required.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }

        if(ConfigUtils.HOOKS_WORLDGUARD.getBoolean()) {
            if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null) {
                getLogger().severe("Could not find WorldGuard! This plugin is required.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }

        if(ConfigUtils.HOOKS_VAULT.getBoolean()) {
            if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
                getLogger().severe("Could not find Vault! This plugin is required.");
                Bukkit.getPluginManager().disablePlugin(this);
            }

            if (!setupEconomy()) {
                getLogger().severe("Disabled due to no Vault dependency found!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);

        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    private void loadFiles() {
        saveDefaultConfig();
        this.lang = new LanguagesFile(this);
        this.lucky = new LuckyBlocksFile(this);
        this.animations = new AnimationsFile(this);
        this.rewards = new RewardsFile(this);
        this.fireworks = new FireworksFile(this);
    }

    private void loadCommands() {
        getCommand("eluckyblock").setExecutor(new MainCommand(this));
        getCommand("eluckyblock").setTabCompleter(new MainCommand(this));
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new FireworkListener(), this);
        this.lucky.getConfig().getKeys(false).forEach(luckyblocks -> {
            Bukkit.getPluginManager().registerEvents(new LuckyBlockListener(this, luckyblocks), this);

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
    }

    private void loadUpdater() {
        if(!ConfigUtils.CHECK_UPDATE_ENABLED.getBoolean()) {
            return;
        }
        String resourceId = "97759";
        UpdateType updateType = UpdateType.valueOf(ConfigUtils.CHECK_UPDATE_TYPE.getString());
        VandalUpdater vandalUpdater = new VandalUpdater(this, resourceId, updateType);
        vandalUpdater.setUpdateMessage(LangUtils.UPDATE_NOTIFICATION.getCustomString());
        vandalUpdater.runTaskTimerAsynchronously(this, 20L, 30 * 60 * 20L);
    }

    public void stopTitleTask(final Player p) {
        if(titleTask.containsKey(p)) {
            titleTask.get(p).cancel();
            titleTask.remove(p);
            titleTicks.put(p, 0);
        }
    }

    public void stopActionTask(final Player p) {
        if(actionTask.containsKey(p)) {
            actionTask.get(p).cancel();
            actionTask.remove(p);
            actionTicks.put(p, 0);
        }
    }

    public void stopBossBarTask(final Player p) {
        if(bossBarTask.containsKey(p)) {
            bossBarTask.get(p).cancel();
            bossBarTask.remove(p);
            bossBarTicks.put(p, 0);
        }
    }

    public void stopBossTimesTask(final Player p) {
        if(bossTimes.containsKey(p)) {
            bossTimes.get(p).removePlayer(p);
            bossTimes.remove(p);
        }
        if(bossTimesTask.containsKey(p)) {
            bossTimesTask.get(p).cancel();
            bossTimesTask.remove(p);
        }
    }

    public void stopFireworkTask(final Player p) {
        if(fireworkTask.containsKey(p)) {
            fireworkTask.get(p).cancel();
            fireworkTask.remove(p);
            fireworkTicks.put(p, 0);
        }
    }

}
