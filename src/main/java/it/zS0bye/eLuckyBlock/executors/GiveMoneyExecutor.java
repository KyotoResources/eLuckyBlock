package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.entity.Player;

public class GiveMoneyExecutor extends Executors {

    private final String execute;
    private final Player player;

    public GiveMoneyExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected String getType() {
        return "[GIVE_MONEY] ";
    }

    @Override
    protected void apply() {

        double money = Double.parseDouble(execute
                .replace(getType(), ""));

        HooksManager.giveMoney(player, money);

    }
}
