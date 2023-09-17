package it.zS0bye.eLuckyBlock.hooks;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.mysql.tables.ScoreTable;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HPlaceholderAPI extends PlaceholderExpansion {

    private final ELuckyBlock plugin;
    private final ScoreTable score;

    public HPlaceholderAPI(final ELuckyBlock plugin) {
        this.plugin = plugin;
        this.score = this.plugin.getScoreTable();
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
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(final OfflinePlayer player, final String params) {
        if (params.equalsIgnoreCase("breaks")) {
            return this.score.getScoreMap(player.getName()) + "";
        }

        return null;
    }

    public String replacePlaceholders(final Player player, String text) {
        return StringUtils.colorize(PlaceholderAPI.setPlaceholders(player, text));
    }
}