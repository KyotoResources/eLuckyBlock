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

package it.zS0bye.eLuckyBlock.hooks.enums;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import lombok.Getter;
import org.bukkit.Bukkit;

public enum Hooks {
    PLACEHOLDERAPI("PlaceholderAPI"),
    WORLDGUARD("WorldGuard"),
    WORLDEDIT("WorldEdit"),
    VAULT("Vault"),
    TOKENENCHANT("TokenEnchant"),
    TOKENMANAGER("TokenManager"),
    PLOTSQUARED("PlotSquared");

    private final ELuckyBlock plugin;
    private final String path;

    @Getter
    private boolean check;

    Hooks(final String path) {
        this.plugin = ELuckyBlock.getInstance();
        this.path = path;
    }

    public void load() {
        if(!Config.valueOf("HOOKS_" + path.toUpperCase()).getBoolean()) {
            check = false;
            return;
        }

        if (Bukkit.getPluginManager().getPlugin(path) != null) {
            check = true;
            return;
        }

        this.plugin.getLogger().severe("Could not find " + path + " This plugin is required.");
        Bukkit.getPluginManager().disablePlugin(this.plugin);
        check = false;
    }
}
