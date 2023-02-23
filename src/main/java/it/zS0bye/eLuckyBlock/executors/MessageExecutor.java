package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import org.bukkit.entity.Player;

public class MessageExecutor extends Executors {

    private final String execute;
    private final Player player;
    private final HooksManager hooks;

    public MessageExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[MESSAGE] ";
    }

    protected void apply() {

        final String msg = this.hooks.getPlaceholders(this.player, execute
                .replace(this.getType(), "")
                .replace("%prefix%", Config.SETTINGS_PREFIX.getString()));

        player.sendMessage(msg);
    }
}