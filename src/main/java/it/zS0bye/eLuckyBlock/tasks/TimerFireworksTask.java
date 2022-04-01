package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.objects.LaunchFirework;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimerFireworksTask extends BukkitRunnable {

    private final ELuckyBlock plugin;
    private final Player player;
    private Location location;
    private String type;
    private List<String> colors;
    private int times;

    private final static Map<Player, Integer> ticks = new HashMap<>();
    private final static Map<Player, BukkitTask> task = new HashMap<>();

    public TimerFireworksTask(final ELuckyBlock plugin, final Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public TimerFireworksTask(final ELuckyBlock plugin, final Player player, final Location location, final String type, final List<String> colors, final int times) {
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        this.type = type;
        this.colors = colors;
        this.times = times;
    }

    @Override
    public void run() {

        if(ticks.get(player) >= times) {
            stopTask();
        }
        new LaunchFirework(plugin, location, type, colors);
        ticks.put(player, ticks.get(player)+1);

    }

    public Map<Player, BukkitTask> getTask() {
        return task;
    }

    public Map<Player, Integer> getTicks() {
        return ticks;
    }

    public void stopTask() {
        if(task.containsKey(this.player)) {
            task.get(this.player).cancel();
            task.remove(this.player);
            ticks.put(this.player, 0);
        }
    }
}
