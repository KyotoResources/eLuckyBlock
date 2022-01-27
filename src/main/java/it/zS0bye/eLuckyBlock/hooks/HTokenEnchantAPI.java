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
