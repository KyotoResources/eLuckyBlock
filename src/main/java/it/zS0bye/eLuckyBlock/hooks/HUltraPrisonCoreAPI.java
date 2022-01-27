package it.zS0bye.eLuckyBlock.hooks;

import me.drawethree.ultraprisoncore.api.enums.LostCause;
import me.drawethree.ultraprisoncore.api.enums.ReceiveCause;
import me.drawethree.ultraprisoncore.tokens.UltraPrisonTokens;
import me.drawethree.ultraprisoncore.tokens.api.UltraPrisonTokensAPI;
import org.bukkit.OfflinePlayer;

public class HUltraPrisonCoreAPI {

    private final UltraPrisonTokensAPI api;
    private final OfflinePlayer player;
    private final double amount;

    public HUltraPrisonCoreAPI(final OfflinePlayer player, final double amount) {
        this.api = UltraPrisonTokens.getInstance().getApi();
        this.player = player;
        this.amount = amount;
    }

    public void addTokens() {
        this.api.addTokens(player, (long)amount, ReceiveCause.GIVE);
    }

    public void takeTokens() {
        this.api.removeTokens(player, (long)amount, LostCause.ADMIN);
    }
}
