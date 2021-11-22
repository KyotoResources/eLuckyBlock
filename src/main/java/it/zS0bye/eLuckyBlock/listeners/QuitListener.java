package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.database.SQLLuckyBlock;
import it.zS0bye.eLuckyBlock.eLuckyBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class QuitListener implements Listener {

    private final SQLLuckyBlock sql;
    private final Map<String, Integer> luckyBreaks;

    public QuitListener(final eLuckyBlock plugin) {
        this.sql = plugin.getSqlLuckyBlock();
        this.luckyBreaks = plugin.getLuckyBreaks();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if(luckyBreaks.containsKey(player.getName())) {
            this.sql.addLuckyBreaks(player.getName(), luckyBreaks.get(player.getName()));
        }
    }
}
