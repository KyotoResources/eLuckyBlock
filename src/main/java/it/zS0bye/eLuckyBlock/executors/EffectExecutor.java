package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class EffectExecutor extends Executors {

    private final Player player;
    private String execute;

    public EffectExecutor(final String execute, final Player player) {
        this.player = player;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[EFFECT] ";
    }

    protected void apply() {
        final String effect = execute.split(";")[0].toUpperCase();
        final int level = Integer.parseInt(execute.split(";")[1]);
        final int time = Integer.parseInt(execute.split(";")[2]);
        player.addPotionEffect(new PotionEffect(ConvertUtils.getPotion(effect), time, level));
    }
}