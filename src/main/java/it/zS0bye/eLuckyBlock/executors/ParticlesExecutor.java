package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticlesExecutor extends Executors {

    private final Player player;
    private final Location location;
    private String execute;

    public ParticlesExecutor(final String execute, final Player player, final Location location) {
        this.player = player;
        this.location = location;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    @Override
    protected String getType() {
        return "[PARTICLES] ";
    }

    @Override
    protected void apply() {

        if(this.execute.startsWith("block-")) {
            String block = this.execute.split("block-")[1];
            this.player.playEffect(this.location, Effect.STEP_SOUND, ConvertUtils.getMaterial(block.toUpperCase()));
            return;
        }

        this.player.playEffect(this.location, ConvertUtils.getParticles(this.execute), ConvertUtils.getParticles(this.execute).getData());
    }
}
