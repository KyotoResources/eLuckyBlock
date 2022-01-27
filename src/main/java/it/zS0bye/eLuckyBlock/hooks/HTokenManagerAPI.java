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
