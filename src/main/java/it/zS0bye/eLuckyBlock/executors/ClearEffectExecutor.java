package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import org.bukkit.entity.Player;

public class ClearEffectExecutor extends Executors {

    private final Player player;
    private String execute;

    public ClearEffectExecutor(final String execute, final Player player) {
        this.player = player;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[CLEAR_EFFECT] ";
    }

    protected void apply() {

        if(this.execute.equalsIgnoreCase("all")) {
            this.player.getActivePotionEffects().forEach(effects ->
                    this.player.removePotionEffect(effects.getType()));
            return;
        }

        player.removePotionEffect(ConvertUtils.getPotion(this.execute));
    }
}