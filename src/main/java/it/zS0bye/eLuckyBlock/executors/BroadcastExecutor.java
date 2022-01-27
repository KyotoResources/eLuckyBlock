package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastExecutor extends Executors {

    private final String execute;
    private final Player player;

    public BroadcastExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[BROADCAST] ";
    }

    protected void apply() {

        String msg = StringUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%player%", player.getName())
                .replace("%prefix%", Config.SETTINGS_PREFIX.getString()));

        Bukkit.getOnlinePlayers().forEach(players ->
                players.sendMessage(msg));

    }
}