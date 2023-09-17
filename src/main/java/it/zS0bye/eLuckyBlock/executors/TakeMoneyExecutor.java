package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;

public class TakeMoneyExecutor extends Executors {

    private final Player player;
    private final HooksManager hooks;
    private String execute;

    public TakeMoneyExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    @Override
    protected String getType() {
        return "[TAKE_MONEY] ";
    }

    @Override
    protected void apply() {
        this.hooks.takeMoney(player, NumberUtils.toDouble(this.execute));
    }
}
