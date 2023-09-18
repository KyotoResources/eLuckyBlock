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
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class HVaultAPI {

    private final ELuckyBlock plugin;
    private Economy api;
    private final OfflinePlayer player;
    private final double amount;

    public HVaultAPI(final OfflinePlayer player, final double amount) {
        this.plugin = ELuckyBlock.getInstance();
        this.player = player;
        this.amount = amount;
        check();
    }

    private void check() {
        if (!setupEconomy()) {
            this.plugin.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this.plugin);
        }
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (economyProvider != null) {
            api = economyProvider.getProvider();
        }

        return (api != null);
    }

    public void addMoney() {
        this.api.depositPlayer(player, amount);
    }

    public void takeMoney() {
        this.api.withdrawPlayer(player, amount);
    }
}
