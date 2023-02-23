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
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[EFFECT] ";
    }

    protected void apply() {

        String effect = execute
                .replace(getType(), "")
                .split(";")[0]
                .toUpperCase();
        int level = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[1]);
        int time = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[2]);

        player.addPotionEffect(new PotionEffect(ConvertUtils.getPotion(effect), time, level));

    }
}