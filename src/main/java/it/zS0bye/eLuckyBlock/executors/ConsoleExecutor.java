package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private final HooksManager hooks;

    public ConsoleExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[CONSOLE] ";
    }

    protected void apply() {

        final String command = this.hooks.getPlaceholders(this.player, execute
                .replace(this.getType(), "")
                .replace("%player%", player.getName()));

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command), 2L);
    }
}