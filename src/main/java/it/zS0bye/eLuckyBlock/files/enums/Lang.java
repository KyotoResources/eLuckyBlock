/*
 * The LuckyBlock Plugin you needed! - https://github.com/KyotoResources/eLuckyBlock
 * Copyright (c) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.zS0bye.eLuckyBlock.files.enums;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.files.IFiles;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Lang implements IFiles {
    PLAYER_NOT_FOUND("player_not_found", "%prefix%&cPlayer not found."),
    IS_NOT_NUMBER("is_not_number", "%prefix%&cPlease enter a valid number."),
    ONLY_POSITIVE_NUMBERS("only_positive_numbers", "%prefix%&cThe number entered must be greater than 0, and cannot be negative!"),
    INSUFFICIENT_PERMISSIONS("insufficient_permissions", "%prefix%&cYou don''t have enough permissions."),
    UPDATE_NOTIFICATION("update_notification", "%prefix%A new update is available! Download version &d%new%&7 from &dhttps://www.spigotmc.org/resources/eluckyblock.97759/&7, you currently have version &d%old%&7."),
    HELP_USERS_TEXTS("Help_Command.users.texts", ",&5 ┃ &d%version%,,&5 ┃ &e[] &7⁼ &7Optional,&5 ┃ &c‹› &7⁼ &7Required,,&5 ┃ &f/%command% help &7‑ &dOpen this page.,&5 ┃ &f/%command% about &7‑ &dShow plug-in info.,&5 ┃ &f/%command% reload &7‑ &dReload configurations.,&5 ┃ &f/%command% info &e[player] &7‑ &dShow the breaks LuckyBlocks.,&5 ┃ &f/%command% give &c<player> <lucky> &e[amount] &7‑ &dGive a player some LuckyBlocks.,,&5 ┃ &d%author%, "),
    RELOAD_ADMINS_CONFIGURATIONS("Reload_Command.admins.configurations", "%prefix%The configurations have been reloaded!"),
    INFO_USERS_CURRENT_BREAKS("Info_Command.users.current_breaks", "%prefix%You have &d%lbBreaks% &7breaks LuckyBlocks."),
    INFO_ADMINS_PLAYER_BREAKS("Info_Command.admins.player_breaks", "%prefix%&f%player%&7 has &d%lbBreaks% &7breaks LuckyBlocks."),
    GIVE_ADMINS_SENDER("Give_Command.admins.sender", "%prefix%You gave &d%luckyblock% x%amount% &7to &d%receiver%&7."),
    GIVE_ADMINS_RECEIVER("Give_Command.admins.receiver", "%prefix%You received &d%luckyblock% x%amount% &7by &d%sender%&7."),
    GIVE_ERRORS_NOT_UNIQUE("Give_Command.errors.not_unique", "%prefix%&cYou cannot give a luckyblock that is not unique!"),
    GIVE_ERRORS_NOT_EXIST("Give_Command.errors.not_exist", "%prefix%&cThis LuckyBlock does not exist!"),
    CITEM_ADMINS_CREATE("CItem_Command.admins.create", "%prefix%You have added the item \"&b%item%&7\" between the custom items."),
    CITEM_ADMINS_UPDATE("CItem_Command.admins.update", "%prefix%You have updated the item \"&b%item%&7\" between the custom items.");

    private final ELuckyBlock plugin;
    private final String path;
    private final String def;
    private FileConfiguration lang;

    Lang(String path, String def) {
        this.path = path;
        this.def = def;
        this.plugin = ELuckyBlock.getInstance();
        this.lang = plugin.getLang().getConfig();
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
        this.lang = this.plugin.getLang().getConfig();
    }

    @Override
    public String getString(final String... var) {
        if(contains(var))
            return StringUtils.getColor(this.lang.getString(variables(var).toString()));
        return StringUtils.getColor(def);
    }

    @Override
    public List<String> getStringList(final String... var) {
        List<String> list = new ArrayList<>();
        if(!contains(var)) {
            if (def.contains(",")) {
                for (String setList : def.split(",")) {
                    list.add(StringUtils.getColor(setList));
                }
            }
            return list;
        }

        for (String setList : this.lang.getStringList(variables(var).toString())) {
            list.add(StringUtils.getColor(setList));
        }

        return list;
    }

    @Override
    public boolean getBoolean(final String... var) {
        if(contains(var))
            return this.lang.getBoolean(variables(var).toString());
        return Boolean.parseBoolean(def);
    }

    @Override
    public boolean contains(final String... var) {
        return this.lang.contains(variables(var).toString());
    }

    @Override
    public int getInt(final String... var) {
        if(contains(var))
            return this.lang.getInt(variables(var).toString());
        return Integer.parseInt(def);
    }

    @Override
    public double getDouble(final String... var) {
        if(contains(var))
            return this.lang.getDouble(variables(var).toString());
        return Double.parseDouble(def);
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
    public void send(final CommandSender sender, final String... var) {
        if (getCustomString(var).isEmpty()) {
            return;
        }
        sender.sendMessage(getCustomString(var));
    }

    @Override
    public Set<String> getKeys() {
        return this.lang.getKeys(false);
    }

    @Override
    public Set<String> getConfigurationSection(final String... var) {
        return this.lang.getConfigurationSection(variables(var).toString()).getKeys( false);
    }
}
