package it.zS0bye.eLuckyBlock.executors;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SpawnMobExecutor extends Executors {

    private final String execute;
    private final Location location;

    public SpawnMobExecutor(final String execute, final Location location) {
        this.execute = execute;
        this.location = location;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[SPAWNMOB] ";
    }

    protected void apply() {

        final String mob = execute
                .replace(this.getType(), "")
                .toUpperCase();

        if(this.location.getWorld() == null) return;

        this.location.getWorld().spawnEntity(this.location, EntityType.valueOf(mob));
    }
}