package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.entity.Player;

public class TakeMoneyExecutor extends Executors {

    private final String execute;
    private final Player player;
    private final HooksManager hooks;

    public TakeMoneyExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    @Override
    protected String getType() {
        return "[TAKE_MONEY] ";
    }

    @Override
    protected void apply() {

        final double money = Double.parseDouble(execute
                .replace(this.getType(), ""));

        this.hooks.takeMoney(player, money);
    }
}
