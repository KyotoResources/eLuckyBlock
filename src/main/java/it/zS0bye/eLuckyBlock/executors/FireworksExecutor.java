package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Fireworks;
import it.zS0bye.eLuckyBlock.LaunchFirework;
import it.zS0bye.eLuckyBlock.tasks.TimerFireworksTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class FireworksExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final Player player;
    private final Location location;
    private final TimerFireworksTask task;
    private String execute;

    public FireworksExecutor(final ELuckyBlock plugin, final String execute, final Player player, final Location location) {
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        this.task = new TimerFireworksTask(this.plugin, this.player);
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    @Override
    protected void startTask(final String type, final List<String> colors, final int delay, final int times) {
        super.startTask(type, colors, delay, times);

        final TimerFireworksTask task = new TimerFireworksTask(this.plugin, this.player, this.location, type, colors, times);

        task.getTicks().put(this.player, 0);
        task.getTask().put(this.player, task.runTaskTimer(this.plugin, delay, 20L));
    }

    @Override
    protected String getType() {
        return "[LAUNCH_FIREWORKS] ";
    }

    @Override
    protected void apply() {

        final int times = Fireworks.TIMES.getInt(this.execute);
        final List<String> colors = Fireworks.COLORS.getStringList(this.execute);


        if(times == 0) {
            this.task.stopTask();
            new LaunchFirework(this.plugin, this.location, Fireworks.TYPE.getString(this.execute), colors);
            return;
        }

        this.task.stopTask();
        this.startTask(Fireworks.TYPE.getString(this.execute), colors, Fireworks.DELAY.getInt(this.execute), times);
    }
}
