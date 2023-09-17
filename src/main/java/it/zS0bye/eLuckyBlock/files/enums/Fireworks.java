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
    public String getStringNoColor(final String... var) {
        return this.config.getString(this.variables(var));
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

    @Override
    public String getCustomString(final String... var) {
        return this.getCustomString(this.getString(var), var);
    }

    @Override
    public String getCustomString(String replace, final String... var) {
        if (replace.startsWith("%prefix%")) {
            replace = replace.replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
                replace = replace.replace("%center%", "");
                return StringUtils.center(replace);
            }
            return replace;
        }

        if(replace.startsWith("%center%")) {
            replace = replace.replace("%center%", "");
            return StringUtils.center(replace);
        }
        return replace;
    }

    @Override
    public void send(final CommandSender sender, final String... var) {
        if (this.getCustomString(var).isEmpty()) return;
        sender.sendMessage(this.getCustomString(var));
    }

    @Override
    public void send(final CommandSender sender, final Map<String, String> placeholders, final String... var) {
        String message = this.getCustomString(var);
        if (message.isEmpty()) return;
        for (String key : placeholders.keySet()) message = message.replace(key, placeholders.get(key));
        sender.sendMessage(message);
    }

    @Override
    public void sendList(final CommandSender sender, final String... var) {
        if (this.getStringList(var).isEmpty()) return;
        this.getStringList(var).forEach(msg -> sender.sendMessage(this.getCustomString(msg, var)));
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
