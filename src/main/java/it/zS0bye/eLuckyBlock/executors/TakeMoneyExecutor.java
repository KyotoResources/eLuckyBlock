package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.entity.Player;

public class TakeMoneyExecutor extends Executors {

    private final String execute;
    private final Player player;

    public TakeMoneyExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected String getType() {
        return "[TAKE_MONEY] ";
    }

    @Override
    protected void apply() {

        double money = Double.parseDouble(execute
                .replace(getType(), ""));

        HooksManager.takeMoney(player, money);

    }
}
