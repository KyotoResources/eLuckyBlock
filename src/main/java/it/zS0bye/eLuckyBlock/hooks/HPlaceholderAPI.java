package it.zS0bye.eLuckyBlock.hooks;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public class HPlaceholderAPI extends PlaceholderExpansion {

    private final Map<String, Integer> luckyScore;

    public HPlaceholderAPI(final ELuckyBlock plugin) {
        this.luckyScore = plugin.getLuckyScore();
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return "zS0bye";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "eLuckyBlock";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; //
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("breaks")) {
            if(luckyScore.containsKey(player.getName())) {
                return String.valueOf(luckyScore.get(player.getName()));
            }
            return "0";
        }

        return null;
    }
}