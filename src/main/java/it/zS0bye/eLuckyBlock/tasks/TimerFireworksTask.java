package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.methods.LaunchFirework;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;

public class TimerFireworksTask extends BukkitRunnable {

    private final eLuckyBlock plugin;
    private final Map<Player, Integer> fireworkTicks;
    private final Player player;
    private final Location location;
    private final String type;
    private final List<String> colors;
    private final int times;

    public TimerFireworksTask(final eLuckyBlock plugin, final Player player, final Location location, final String type, final List<String> colors, final int times) {
        this.plugin = plugin;
        this.fireworkTicks = plugin.getFireworkTicks();
        this.player = player;
        this.location = location;
        this.type = type;
        this.colors = colors;
        this.times = times;
    }

    @Override
    public void run() {

        if(fireworkTicks.get(player) == times) {
            this.plugin.stopFireworkTask(player);
        }
        new LaunchFirework(plugin, location, type, colors);
        fireworkTicks.put(player, fireworkTicks.get(player)+1);

    }
}
