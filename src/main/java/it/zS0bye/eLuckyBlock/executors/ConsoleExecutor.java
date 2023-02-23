package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ConsoleExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;

    public ConsoleExecutor(final String execute, final Player player) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[CONSOLE] ";
    }

    protected void apply() {

        String command = StringUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%player%", player.getName()));

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }.runTaskLater(this.plugin, 2L);

    }
}