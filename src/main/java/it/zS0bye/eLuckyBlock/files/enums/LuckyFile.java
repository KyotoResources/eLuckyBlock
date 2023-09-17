package it.zS0bye.eLuckyBlock.files.enums;

import it.zS0bye.eLuckyBlock.files.FileManager;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public enum LuckyFile {
    CUSTOM(""),
    MATERIAL("Material"),
    INSTANT_BREAK("Instant_Break"),
    UNIQUE_CHECK_ENABLED("Unique_Check.enabled"),
    UNIQUE_CHECK_NAME("Unique_Check.display_name"),
    UNIQUE_CHECK_LORE("Unique_Check.lore"),
    PREVENTIONS_ENABLED("Preventions.enabled"),
    PREVENTIONS_DENY_PICKUP("Preventions.deny_pickup"),
    PREVENTIONS_DENY_ABSORB("Preventions.deny_absorb"),
    PREVENTIONS_WORLDS_TYPE("Preventions.worlds.type"),
    PREVENTIONS_WORLDS_LIST("Preventions.worlds.list"),
    PREVENTIONS_ALLOWED_GAMEMODES("Preventions.allowed_gamemodes"),
    REWARDS("Rewards"),
    REWARDS_CHANCE(".chance"),
    REWARDS_EXECUTORS(".executors");

    private final String path;

    LuckyFile(final String path) {
        this.path = path;
    }

    public String variables(final String... var) {
        StringBuilder builder = new StringBuilder();
        for(String texts : var) builder.append(texts);
        builder.append(path);
        return builder.toString();
    }

    public String getPath() {
        return this.path;
    }

    public FileConfiguration getConfig(final String luckyblock) {
        return FileManager.getFiles().get(luckyblock.toLowerCase());
    }

    public String getString(final String luckyblock, final String... var) {
        return StringUtils.colorize(this.getConfig(luckyblock).getString(this.variables(var)));
    }

    public String getCustomString(final String luckyblock, final String... var) {

        if (this.getString(luckyblock, var).startsWith("%prefix%")) {
            String replace = this.getString(luckyblock, var).replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
                replace = replace.replace("%center%", "");
                return StringUtils.center(replace);
            }
            return replace;
        }

        if(this.getString(luckyblock, var).startsWith("%center%")) {
            final String replace = this.getString(luckyblock, var).replace("%center%", "");
            return StringUtils.center(replace);
        }

        return this.getString(luckyblock, var);
    }


    public List<String> getStringList(final String luckyblock, final String... var) {
        final List<String> list = new ArrayList<>();
        for (final String setList : this.getConfig(luckyblock).getStringList(this.variables(var))) list.add(StringUtils.colorize(setList));
        return list;
    }

    public boolean getBoolean(final String luckyblock, final String... var) {
        return this.getConfig(luckyblock).getBoolean(this.variables(var));
    }

    public boolean contains(final String luckyblock, final String... var) {
        return this.getConfig(luckyblock).contains(this.variables(var));
    }

    public int getInt(final String luckyblock, final String... var) {
        return this.getConfig(luckyblock).getInt(this.variables(var));
    }

    public double getDouble(final String luckyblock, final String... var) {
        return this.getConfig(luckyblock).getDouble(this.variables(var));
    }

    public void send(final CommandSender sender, final String luckyblock, final String... var) {
        if (this.getCustomString(luckyblock, var).isEmpty()) return;
        sender.sendMessage(this.getCustomString(luckyblock, var));
    }

    public Set<String> getKeys(final String luckyblock) {
        return this.getConfig(luckyblock).getKeys(false);
    }

    @SuppressWarnings("all")
    public Set<String> getConfigurationSection(final String luckyblock, final String... var) {
        return this.getConfig(luckyblock).getConfigurationSection(this.variables(var)).getKeys( false);
    }

}
