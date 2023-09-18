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
        this.config = plugin.getFireworks().getConfig();
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
        this.config = this.plugin.getFireworks().getConfig();
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
    public double getDouble(final String... var) {
        return this.config.getDouble(variables(var).toString());
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
        return this.config.getKeys(false);
    }

    @Override
    public Set<String> getConfigurationSection(final String... var) {
        return this.config.getConfigurationSection(variables(var).toString()).getKeys( false);
    }
}
