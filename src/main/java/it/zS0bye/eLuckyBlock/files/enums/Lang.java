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

public enum Lang implements IFiles {
    PLAYER_NOT_FOUND("player_not_found"),
    NOT_A_PLAYER("not_a_player"),
    IS_NOT_NUMBER("is_not_number"),
    ONLY_POSITIVE_NUMBERS("only_positive_numbers"),
    INSUFFICIENT_PERMISSIONS("insufficient_permissions"),
    UPDATE_NOTIFICATION("update_notification"),
    HELP_USERS_TEXTS("Help_Command.users.texts"),
    RELOAD_CONFIGURATIONS("Reload_Command.configurations"),
    INPUT_GUI_SUCCESSFULL("Input_GUI.successfull"),
    INPUT_GUI_ERRORS_REGEX_NOT_VALID("Input_GUI.errors.regex_not_valid"),
    INPUT_GUI_PATTERNS_PERMISSION("Input_GUI.patterns.permission"),
    SETTINGS_GUI_TOGGLE("Settings_GUI.toggle"),
    SETTINGS_GUI_SET_MATERIAL("Settings_GUI.set_material"),
    SETTINGS_GUI_CANCEL_SAVES("Settings_GUI.cancel_saves"),
    SETTINGS_GUI_ERRORS_INVALID_MATERIAL("Settings_GUI.errors.invalid_material"),
    SETTINGS_GUI_STATUS_ENABLED("Settings_GUI.status.enabled"),
    SETTINGS_GUI_STATUS_DISABLED("Settings_GUI.status.disabled"),
    INFO_USERS_CURRENT_BREAKS("Info_Command.users.current_breaks"),
    INFO_ADMINS_PLAYER_BREAKS("Info_Command.admins.player_breaks"),
    GIVE_ADMINS_SENDER("Give_Command.admins.sender"),
    GIVE_ADMINS_RECEIVER("Give_Command.admins.receiver"),
    GIVE_ERRORS_NOT_UNIQUE("Give_Command.errors.not_unique"),
    GIVE_ERRORS_NOT_EXIST("Give_Command.errors.not_exist"),
    CITEM_ADMINS_CREATE("CItem_Command.admins.create"),
    CITEM_ADMINS_UPDATE("CItem_Command.admins.update");

    private final ELuckyBlock plugin;
    private final String path;
    private FileConfiguration config;

    Lang(final String path) {
        this.path = path;
        this.plugin = ELuckyBlock.getInstance();
        this.reloadConfig();
    }

    @Override
    public String getPath() {
        return this.path;
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
    public void reloadConfig() {
        this.config = this.plugin.getLanguagesFile().getConfig();
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
        return this.config.getBoolean(variables(var));
    }

    @Override
    public boolean contains(final String... var) {
        return this.config.contains(variables(var));
    }

    @Override
    public int getInt(final String... var) {
        return this.config.getInt(variables(var));
    }

    @Override
    public double getDouble(final String... var) {
        return this.config.getDouble(variables(var));
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
