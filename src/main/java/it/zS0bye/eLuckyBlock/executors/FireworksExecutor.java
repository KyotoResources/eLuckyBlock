package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.methods.LaunchFirework;
import it.zS0bye.eLuckyBlock.tasks.TimerFireworksTask;
import it.zS0bye.eLuckyBlock.utils.FireworkUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;

public class FireworksExecutor extends Executors {

    private final Map<Player, Integer> fireworkTicks;
    private final Map<Player, BukkitTask> fireworkTask;
    private final eLuckyBlock plugin;
    private final String execute;
    private final Player player;
    private final Location location;

    public FireworksExecutor(final String execute, final Player player, final Location location) {
        this.plugin = eLuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.location = location;
        this.fireworkTicks = plugin.getFireworkTicks();
        this.fireworkTask = plugin.getFireworkTask();
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String type, List<String> colors, int delay, int times) {
        super.startTask(type, colors, delay, times);

        fireworkTicks.put(this.player, 0);
        fireworkTask.put(this.player,
                new TimerFireworksTask(this.plugin, this.player, this.location, type, colors, times)
                        .runTaskTimer(this.plugin, delay, 20L));
    }

    @Override
    protected String getType() {
        return "[LAUNCH_FIREWORKS] ";
    }

    @Override
    protected void apply() {

        String name = execute
                .replace(getType(), "");

        FireworkUtils fireworks = new FireworkUtils(name);

        String type = fireworks.getString(fireworks.getType());
        int times = fireworks.getInt(fireworks.getTimes());
        int delay = fireworks.getInt(fireworks.getDelay());
        List<String> colors = fireworks.getStringList(fireworks.getColors());


        if(times == 0) {
            new LaunchFirework(this.plugin, this.location, type, colors);
            return;
        }

        startTask(type, colors, delay, times);
    }
}
