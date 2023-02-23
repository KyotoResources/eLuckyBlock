package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import org.bukkit.entity.Player;

public class ClearEffectExecutor extends Executors {

    private final String execute;
    private final Player player;

    public ClearEffectExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[CLEAR_EFFECT] ";
    }

    protected void apply() {

        String effect = execute
                .replace(getType(), "");

        if(effect.equalsIgnoreCase("all")) {
            this.player.getActivePotionEffects().forEach(effects ->
                    this.player.removePotionEffect(effects.getType()));
            return;
        }

        player.removePotionEffect(ConvertUtils.getPotion(effect));

    }
}