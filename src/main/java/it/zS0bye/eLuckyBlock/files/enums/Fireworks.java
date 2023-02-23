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

public enum Fireworks implements IFiles {
    TYPE(".type"),
    TIMES(".times"),
    DELAY(".delay"),
    COLORS(".colors");

    private final ELuckyBlock plugin;
    private final String path;
    private FileConfiguration config;

    Fireworks(final String path) {
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
        this.config = this.plugin.getFireworksFile().getConfig();
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
            final String replace = target.replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
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
            final String replace = target.replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
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
