package it.zS0bye.eLuckyBlock.files.enums;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.files.IFiles;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Animations implements IFiles {
    ANIMATIONS(""),
    INTERVAL(".interval"),
    TEXTS(".texts");

    private final ELuckyBlock plugin;
    private final String path;
    private FileConfiguration config;

    Animations(final String path) {
        this.path = path;
        this.plugin = ELuckyBlock.getInstance();
        this.config = plugin.getAnimations().getConfig();
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
        this.config = this.plugin.getAnimations().getConfig();
    }

    @Override
    public String getString(final String... var) {
        return StringUtils.getColor(this.config.getString(variables(var).toString()));
    }

    @Override
    public String getCustomString(final String... var) {
        if (getString(var).startsWith("%prefix%")) {
            String replace = getString(var).replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
                String replace2 = replace.replace("%center%", "");
                return StringUtils.sendCentered(replace2);
            }
            return replace;
        }
        if(getString(var).startsWith("%center%")) {
            String replace = getString(var).replace("%center%", "");
            return StringUtils.sendCentered(replace);
        }
        return getString(var);
    }

    @Override
    public List<String> getStringList(final String... var) {
        List<String> list = new ArrayList<>();
        for (String setList : this.config.getStringList(variables(var).toString())) {
            list.add(StringUtils.getColor(setList));
        }
        return list;
    }

    @Override
    public boolean getBoolean(final String... var) {
        return this.config.getBoolean(variables(var).toString());
    }

    @Override
    public boolean contains(final String... var) {
        return this.config.contains(variables(var).toString());
    }

    @Override
    public int getInt(final String... var) {
        return this.config.getInt(variables(var).toString());
    }

    @Override
    public void send(final CommandSender sender, final String... var) {
        if (getCustomString().isEmpty()) {
            return;
        }
        sender.sendMessage(getCustomString(var));
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
