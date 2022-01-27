package it.zS0bye.eLuckyBlock.files.enums;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.files.IFiles;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Config implements IFiles {
    SETTINGS_PREFIX("Settings.prefix", " &d❏ ₑLuckyBlock ┃ &7"),
    CHECK_UPDATE_ENABLED("Settings.check_update.enabled", "true"),
    CHECK_UPDATE_TYPE("Settings.check_update.type", "NORMAL"),
    HOOKS_PLACEHOLDERAPI("Settings.hooks.PlaceholderAPI", "false"),
    HOOKS_WORLDGUARD("Settings.hooks.WorldGuard", "false"),
    HOOKS_WORLDEDIT("Settings.hooks.WorldEdit", "false"),
    HOOKS_VAULT("Settings.hooks.Vault", "false"),
    HOOKS_TOKENENCHANT("Settings.hooks.TokenEnchant", "false"),
    HOOKS_TOKENMANAGER("Settings.hooks.TokenManager", "false"),
    HOOKS_ULTRAPRISONCORE("Settings.hooks.UltraPrisonCore", "false"),
    DB_TYPE("Storage.type", "SQLite"),
    DB_NAME("Storage.mysql.database", "eLuckyBlock"),
    DB_HOST("Storage.mysql.hostname", "localhost"),
    DB_PORT("Storage.mysql.port", "3306"),
    DB_USER("Storage.mysql.user", "root"),
    DB_PASSWORD("Storage.mysql.password", "MyPassword"),
    DB_CUSTOMURI("Storage.mysql.advanced.customURI", "jdbc:mysql://%host%:%port%/%database%?useSSL=false");

    private final String path;
    private final String def;
    private final ELuckyBlock plugin;
    private FileConfiguration config;

    Config(String path, String def) {
        this.path = path;
        this.def = def;
        this.plugin = ELuckyBlock.getInstance();
        this.config = this.plugin.getConfig();
    }

    @Override
    public StringBuilder variables(final String... var) {
        StringBuilder builder = new StringBuilder();
        for(String texts : var) {
            builder.append(texts);
        }
        builder.append(path);
        return builder;
    }

    @Override
    public void reloadConfig() {
        this.config = this.plugin.getConfig();
    }

    @Override
    public String getString(final String... var) {
        if(contains())
            return StringUtils.getColor(this.config.getString(variables(var).toString()));
        return StringUtils.getColor(def);
    }

    @Override
    public List<String> getStringList(final String... var) {
        List<String> list = new ArrayList<>();
        if(contains()) {
            for (String setList : this.config.getStringList(variables(var).toString())) {
                list.add(StringUtils.getColor(setList));
            }
        }
        if(def.contains(",")) {
            for (String setList : def.split(",")) {
                list.add(StringUtils.getColor(setList));
            }
        }
        return list;
    }

    @Override
    public boolean getBoolean(final String... var) {
        if(contains())
            return this.config.getBoolean(variables(var).toString());
        return Boolean.parseBoolean(def);
    }

    @Override
    public boolean contains(final String... var) {
        return this.config.contains(variables(var).toString());
    }

    @Override
    public int getInt(final String... var) {
        if(contains())
            return this.config.getInt(path);
        return Integer.parseInt(def);
    }

    @Override
    public String getCustomString(final String... var) {
        if (getString(var).startsWith("%prefix%")) {
            String replace = getString(var).replace("%prefix%", SETTINGS_PREFIX.getString());
            if (replace.startsWith(SETTINGS_PREFIX.getString() + "%center%")) {
                String replace2 = replace.replace("%center%", "");
                return StringUtils.sendCentered(replace2);
            }
            return replace;
        }
        if(getString(var).startsWith("%center%")) {
            String replace = getString(var).replace("%center%", "");
            return StringUtils.sendCentered(replace);
        }
        return getString();
    }

    @Override
    public void send(final CommandSender sender, final String... var) {
        if (getCustomString().isEmpty()) {
            return;
        }
        sender.sendMessage(getCustomString());
    }

    @Override
    public Set<String> getKeys() {
        return this.config.getKeys(false);
    }

    @Override
    public Set<String> getConfigurationSection(final String... var) {
        return this.config.getConfigurationSection(variables(var).toString()).getKeys( false);
    }

}
