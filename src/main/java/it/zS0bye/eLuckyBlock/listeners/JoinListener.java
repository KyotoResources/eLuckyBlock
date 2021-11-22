package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.database.SQLLuckyBlock;
import it.zS0bye.eLuckyBlock.eLuckyBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class JoinListener implements Listener {

    private final SQLLuckyBlock sql;
    private final Map<String, Integer> luckyBreaks;

    public JoinListener(final eLuckyBlock plugin) {
        this.sql = plugin.getSqlLuckyBlock();
        this.luckyBreaks = plugin.getLuckyBreaks();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(!luckyBreaks.containsKey(player.getName())) {
            this.sql.getLuckyBreaks(player.getName()).thenAccept(getLuckyBreaks -> luckyBreaks.put(player.getName(), getLuckyBreaks));
        }
    }
}
