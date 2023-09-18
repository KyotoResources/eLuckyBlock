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

import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import org.bukkit.OfflinePlayer;

public class HTokenEnchantAPI {

    private final TokenEnchantAPI api;
    private final OfflinePlayer player;
    private final double amount;

    public HTokenEnchantAPI(final OfflinePlayer player, final double amount) {
        this.api = TokenEnchantAPI.getInstance();
        this.player = player;
        this.amount = amount;
    }

    public void addTokens() {
        this.api.addTokens(player, amount);
    }

    public void takeTokens() {
        this.api.removeTokens(player, amount);
    }
}
