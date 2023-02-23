package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.entity.Player;

public class SudoExecutor extends Executors {

    private final String execute;
    private final Player player;
    private final HooksManager hooks;

    public SudoExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[SUDO] ";
    }

    protected void apply() {

        final String sudo = this.hooks.getPlaceholders(this.player, execute
                .replace(this.getType(), ""));

        player.chat(sudo);
    }
}