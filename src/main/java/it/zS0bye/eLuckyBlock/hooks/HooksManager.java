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

package it.zS0bye.eLuckyBlock.hooks;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.enums.Hooks;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.io.File;
import java.util.UUID;

@Getter
public class HooksManager {

    private final ELuckyBlock plugin;

    public HooksManager(final ELuckyBlock plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        PlaceholderAPI();
        Hooks.WORLDGUARD.load();
        Hooks.WORLDEDIT.load();
        Hooks.TOKENENCHANT.load();
        Hooks.TOKENMANAGER.load();
        Hooks.PLOTSQUARED.load();
    }

    private void PlaceholderAPI() {
        Hooks.PLACEHOLDERAPI.load();
        if(Hooks.PLACEHOLDERAPI.isCheck())
            new HPlaceholderAPI(this.plugin).register();
    }

    public static void giveMoney(final OfflinePlayer player, final double amount) {
        if(Hooks.VAULT.isCheck())
            new HVaultAPI(player, amount).addMoney();
    }

    public static void takeMoney(final OfflinePlayer player, final double amount) {
        if(Hooks.VAULT.isCheck())
            new HVaultAPI(player, amount).takeMoney();
    }

    public static void giveTokens(final OfflinePlayer player, final double amount) {
        if(Hooks.TOKENENCHANT.isCheck())
            new HTokenEnchantAPI(player, amount).addTokens();
        if(Hooks.TOKENMANAGER.isCheck())
            new HTokenManagerAPI(player, amount).addTokens();
    }

    public static void takeTokens(final OfflinePlayer player, final double amount) {
        if(Hooks.TOKENENCHANT.isCheck())
            new HTokenEnchantAPI(player, amount).takeTokens();
        if(Hooks.TOKENMANAGER.isCheck())
            new HTokenManagerAPI(player, amount).takeTokens();
    }

    public static void loadAndCopySchem(final File file, final World world, final Location loc) {
        if(Hooks.WORLDEDIT.isCheck())
            new HWorldEditAPI(file, world, loc).loadAndCopy();
    }

    public static void pasteSchem(final File file, final World world, final Location loc) {
        if(Hooks.WORLDEDIT.isCheck())
            new HWorldEditAPI(file, world, loc).paste();
    }

    public static boolean checkPlot(final UUID uuid) {
        if(Hooks.PLOTSQUARED.isCheck())
            return new HPlotSquaredAPI(uuid).checkPlot();
        return false;
    }


}
