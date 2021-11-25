package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.utils.ConfigUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class TakeMoneyExecutor extends Executors {

    private final String execute;
    private final Player player;
    private final Economy vault;

    public TakeMoneyExecutor(final String execute, final Player player) {
        this.vault = eLuckyBlock.getInstance().getEconomy();
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

        if(ConfigUtils.HOOKS_VAULT.getBoolean())
            this.vault.withdrawPlayer(player, money);

    }
}
