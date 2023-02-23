package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class EffectExecutor extends Executors {

    private final String execute;
    private final Player player;

    public EffectExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[EFFECT] ";
    }

    protected void apply() {

        final String effect = execute
                .replace(this.getType(), "")
                .split(";")[0]
                .toUpperCase();
        final int level = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[1]);
        final int time = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[2]);

        player.addPotionEffect(new PotionEffect(ConvertUtils.getPotion(effect), time, level));
    }
}