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

import me.realized.tokenmanager.api.TokenManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class HTokenManagerAPI {

    private final TokenManager api;
    private final OfflinePlayer player;
    private final double amount;

    public HTokenManagerAPI(final OfflinePlayer player, final double amount) {
        this.api = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
        this.player = player;
        this.amount = amount;
    }

    public void addTokens() {
        this.api.addTokens(player.getName(), (long)amount);
    }

    public void takeTokens() {
        this.api.removeTokens(player.getName(), (long)amount);
    }
}
