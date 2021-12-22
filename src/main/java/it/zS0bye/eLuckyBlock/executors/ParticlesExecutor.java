package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticlesExecutor extends Executors {

    private final String execute;
    private final Player player;
    private final Location location;

    public ParticlesExecutor(final String execute, final Player player, final Location location) {
        this.execute = execute;
        this.player = player;
        this.location = location;
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected String getType() {
        return "[PARTICLES] ";
    }

    @Override
    protected void apply() {

        String particles = execute
                .replace(getType(), "");

        this.player.playEffect(this.location,
                ConvertUtils.getParticles(particles),
                ConvertUtils.getParticles(particles).getData());

    }
}
