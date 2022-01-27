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
