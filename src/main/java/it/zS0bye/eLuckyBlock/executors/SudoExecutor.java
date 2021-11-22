package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import org.bukkit.entity.Player;

public class SudoExecutor extends Executors {

    private final String execute;
    private final Player player;

    public SudoExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[SUDO] ";
    }

    protected void apply() {

        String sudo = ColorUtils.getPapi(this.player, execute
                .replace(getType(), ""));

        player.chat(sudo);

    }
}