package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import it.zS0bye.eLuckyBlock.utils.ConfigUtils;
import org.bukkit.entity.Player;

public class MessageExecutor extends Executors {

    private final String execute;
    private final Player player;

    public MessageExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[MESSAGE] ";
    }

    protected void apply() {

        String msg = ColorUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%prefix%", ConfigUtils.SETTINGS_PREFIX.getString()));

        player.sendMessage(msg);
    }
}