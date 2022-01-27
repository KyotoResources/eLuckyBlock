package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerExecutor extends Executors {

    private final String execute;
    private final Player player;

    public PlayerExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[PLAYER] ";
    }

    protected void apply() {

        String command = StringUtils.getPapi(this.player, execute
                .replace(getType(), ""));

        Bukkit.dispatchCommand(player, command);

    }
}