package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleExecutor extends Executors {

    private final String execute;
    private final Player player;

    public ConsoleExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[CONSOLE] ";
    }

    protected void apply() {

        String command = ColorUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%player%", player.getName()));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

    }
}