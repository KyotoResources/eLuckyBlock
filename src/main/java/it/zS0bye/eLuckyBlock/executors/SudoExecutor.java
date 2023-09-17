package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.entity.Player;

public class SudoExecutor extends Executors {

    private final Player player;
    private final HooksManager hooks;
    private String execute;

    public SudoExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[SUDO] ";
    }

    protected void apply() {
        player.chat(this.hooks.getPlaceholders(this.player, this.execute));
    }
}