package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final Player player;
    private final HooksManager hooks;
    private String execute;

    public ConsoleExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[CONSOLE] ";
    }

    protected void apply() {
        final String command = this.hooks.getPlaceholders(this.player, execute.replace("%player%", player.getName()));
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command), 2L);
    }
}