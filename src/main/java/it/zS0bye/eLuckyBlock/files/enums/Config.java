package it.zS0bye.eLuckyBlock.files.enums;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.files.IFiles;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum Config implements IFiles {
    SETTINGS_PREFIX("Settings.prefix"),
    SETTINGS_LOCALE("Settings.locale"),
    CHECK_UPDATE_ENABLED("Settings.check_update.enabled"),
    CHECK_UPDATE_TYPE("Settings.check_update.type"),
    HOOKS_PLACEHOLDERAPI("Settings.hooks.PlaceholderAPI"),
    HOOKS_WORLDGUARD("Settings.hooks.WorldGuard"),
    HOOKS_WORLDEDIT("Settings.hooks.WorldEdit"),
    HOOKS_VAULT("Settings.hooks.Vault"),
    HOOKS_TOKENENCHANT("Settings.hooks.TokenEnchant"),
    HOOKS_TOKENMANAGER("Settings.hooks.TokenManager"),
    HOOKS_PLOTSQUARED("Settings.hooks.PlotSquared"),
    DB_TYPE("Storage.type"),
    DB_NAME("Storage.mysql.database"),
    DB_HOST("Storage.mysql.hostname"),
    DB_PORT("Storage.mysql.port"),
    DB_USER("Storage.mysql.user"),
    DB_PASSWORD("Storage.mysql.password"),
    DB_CUSTOMURI("Storage.mysql.advanced.customURI"),
    DB_MAXIMUM_POOL_SIZE("Storage.pool_settings.maximum_pool_size"),
    DB_MINIMUM_IDLE("Storage.pool_settings.minimum_idle"),
    DB_MAXIMUM_LIFETIME("Storage.pool_settings.maximum_lifetime"),
    DB_KEEPALIVE_TIME("Storage.pool_settings.keepalive_time"),
    DB_CONNECTION_TIMEOUT("Storage.pool_settings.connection_timeout");

    private final String path;
    private final ELuckyBlock plugin;
    private FileConfiguration config;

    Config(final String path) {
        this.path = path;
        this.plugin = ELuckyBlock.getInstance();
        this.reloadConfig();
    }

    @Override
    public String variables(final String... var) {
        StringBuilder builder = new StringBuilder();
        for(String texts : var) {
            builder.append(texts);
        }
        builder.append(path);
        return builder.toString();
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void reloadConfig() {
        this.config = this.plugin.getConfig();
    }

    @Override
    public String getString(final String... var) {
        return StringUtils.colorize(this.config.getString(this.variables(var)));
    }

    @Override
    public List<String> getStringList(final String... var) {
        List<String> list = new ArrayList<>();
        for (String setList : this.config.getStringList(this.variables(var))) list.add(StringUtils.colorize(setList));
        return list;
    }

    @Override
    public boolean getBoolean(final String... var) {
        return this.config.getBoolean(this.variables(var));
    }

    @Override
    public boolean contains(final String... var) {
        return this.config.contains(this.variables(var));
    }

    @Override
    public int getInt(final String... var) {
        return this.config.getInt(this.variables(var));
    }

    @Override
    public double getDouble(final String... var) {
        return this.config.getDouble(this.variables(var));
    }

    @SafeVarargs
    @Override
    public final String getCustomString(final String minVar, final Map<String, String>... placeholders) {

        String target = this.getString(this.convertVar(minVar));

        for (Map<String, String> placeholder : placeholders) {
            for (String key : placeholder.keySet()) target = target.replace(key, placeholder.get(key));
        }

        if (target.startsWith("%prefix%")) {
            final String replace = target.replace("%prefix%", SETTINGS_PREFIX.getString());
            if (replace.startsWith(SETTINGS_PREFIX.getString() + "%center%")) {
                final String replace2 = replace.replace("%center%", "");
                return StringUtils.center(replace2);
            }
            return replace;
        }
        if(target.startsWith("%center%")) {
            final String replace = target.replace("%center%", "");
            return StringUtils.center(replace);
        }
        return target;
    }

    @SafeVarargs
    @Override
    public final String getCustomString(final Map<String, String>... placeholders) {

        String target = this.getString();

        for (Map<String, String> placeholder : placeholders) {
            for (String key : placeholder.keySet()) target = target.replace(key, placeholder.get(key));
        }

        if (target.startsWith("%prefix%")) {
            final String replace = target.replace("%prefix%", SETTINGS_PREFIX.getString());
            if (replace.startsWith(SETTINGS_PREFIX.getString() + "%center%")) {
                final String replace2 = replace.replace("%center%", "");
                return StringUtils.center(replace2);
            }
            return replace;
        }
        if(target.startsWith("%center%")) {
            final String replace = target.replace("%center%", "");
            return StringUtils.center(replace);
        }
        return target;
    }

    @SafeVarargs
    @Override
    public final void send(final CommandSender sender, final String minVar, final Map<String, String>... placeholders) {
        if (this.getCustomString(minVar, placeholders).isEmpty()) return;
        sender.sendMessage(this.getCustomString(minVar, placeholders));
    }

    @SafeVarargs
    @Override
    public final void send(final CommandSender sender, final Map<String, String>... placeholders) {
        if (this.getCustomString(placeholders).isEmpty()) return;
        sender.sendMessage(this.getCustomString(placeholders));
    }

    @Override
    public Set<String> getKeys() {
        return this.config.getKeys(false);
    }

    @SuppressWarnings("all")
    @Override
    public Set<String> getConfigurationSection(final String... var) {
        return this.config.getConfigurationSection(this.variables(var)).getKeys( false);
    }

    private String[] convertVar(final String var) {
        return var.split("\\.");
    }

}
