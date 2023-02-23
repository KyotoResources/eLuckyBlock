package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerExecutor extends Executors {

    private final String execute;
    private final Player player;
    private final HooksManager hooks;

    public PlayerExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[PLAYER] ";
    }

    protected void apply() {

        final String command = this.hooks.getPlaceholders(this.player, execute
                .replace(this.getType(), ""));

        Bukkit.dispatchCommand(player, command);
    }
}