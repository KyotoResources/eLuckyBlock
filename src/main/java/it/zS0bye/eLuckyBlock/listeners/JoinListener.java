package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.mysql.tables.ScoreTable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class JoinListener implements Listener {

    private final ScoreTable score;
    private final Map<String, Integer> luckyScore;

    public JoinListener(final ELuckyBlock plugin) {
        this.score = plugin.getScoreTable();
        this.luckyScore = plugin.getLuckyScore();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if(luckyScore.containsKey(player.getName())) return;
        this.score.getScore(player.getName()).thenAccept(score -> luckyScore.put(player.getName(), score));
    }
}
