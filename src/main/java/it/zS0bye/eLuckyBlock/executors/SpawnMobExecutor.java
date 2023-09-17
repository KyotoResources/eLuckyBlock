package it.zS0bye.eLuckyBlock.executors;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SpawnMobExecutor extends Executors {

    private final Location location;
    private String execute;

    public SpawnMobExecutor(final String execute, final Location location) {
        this.location = location;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[SPAWNMOB] ";
    }

    protected void apply() {
        if(this.location.getWorld() == null) return;
        this.location.getWorld().spawnEntity(this.location, EntityType.valueOf(this.execute.toUpperCase()));
    }
}