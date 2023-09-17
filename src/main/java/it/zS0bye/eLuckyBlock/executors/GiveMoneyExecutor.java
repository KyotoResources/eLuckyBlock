package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;

public class GiveMoneyExecutor extends Executors {

    private final Player player;
    private final HooksManager hooks;
    private String execute;

    public GiveMoneyExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    @Override
    protected String getType() {
        return "[GIVE_MONEY] ";
    }

    @Override
    protected void apply() {
        this.hooks.giveMoney(player, NumberUtils.toDouble(this.execute));
    }
}
