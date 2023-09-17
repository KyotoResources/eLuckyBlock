package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import org.bukkit.entity.Player;

public class MessageExecutor extends Executors {

    private final Player player;
    private final HooksManager hooks;
    private String execute;

    public MessageExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[MESSAGE] ";
    }

    protected void apply() {
        player.sendMessage(this.hooks.getPlaceholders(this.player, execute.replace("%prefix%", Config.SETTINGS_PREFIX.getString())));
    }
}