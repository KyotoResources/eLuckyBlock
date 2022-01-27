package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.database.SQLLuckyBreaks;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class JoinListener implements Listener {

    private final SQLLuckyBreaks sql;
    private final Map<String, Integer> luckyBreaks;

    public JoinListener(final ELuckyBlock plugin) {
        this.sql = plugin.getSqlLuckyBreaks();
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
