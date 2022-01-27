package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.entity.Player;

public class TakeTokensExecutor extends Executors {

    private final String execute;
    private final Player player;

    public TakeTokensExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }


    @Override
    protected String getType() {
        return "[TAKE_TOKENS] ";
    }

    @Override
    protected void apply() {

        double tokens = Double.parseDouble(execute
                .replace(getType(), ""));

        HooksManager.takeTokens(player, tokens);
    }
}
