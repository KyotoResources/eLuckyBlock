package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.entity.Player;

public class GiveTokensExecutor extends Executors {

    private final String execute;
    private final Player player;

    public GiveTokensExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }


    @Override
    protected String getType() {
        return "[GIVE_TOKENS] ";
    }

    @Override
    protected void apply() {

        double tokens = Double.parseDouble(execute
                .replace(getType(), ""));

        HooksManager.giveTokens(player, tokens);
    }
}
