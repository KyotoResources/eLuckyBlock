package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Fireworks;
import it.zS0bye.eLuckyBlock.objects.LaunchFirework;
import it.zS0bye.eLuckyBlock.tasks.TimerFireworksTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class FireworksExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private final Location location;
    private final TimerFireworksTask task;

    public FireworksExecutor(final String execute, final Player player, final Location location) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.location = location;
        this.task = new TimerFireworksTask(this.plugin, this.player);
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String type, List<String> colors, int delay, int times) {
        super.startTask(type, colors, delay, times);

        TimerFireworksTask task = new TimerFireworksTask(this.plugin, this.player, this.location, type, colors, times);

        task.getTicks().put(this.player, 0);
        task.getTask().put(this.player,
                task.runTaskTimer(this.plugin, delay, 20L));
    }

    @Override
    protected String getType() {
        return "[LAUNCH_FIREWORKS] ";
    }

    @Override
    protected void apply() {

        String name = execute
                .replace(getType(), "");

        int times = Fireworks.TIMES.getInt(name);
        List<String> colors = Fireworks.COLORS.getStringList(name);


        if(times == 0) {
            this.task.stopTask();
            new LaunchFirework(this.plugin, this.location, Fireworks.TYPE.getString(name), colors);
            return;
        }

        this.task.stopTask();
        startTask(Fireworks.TYPE.getString(name), colors, Fireworks.DELAY.getInt(name), times);
    }
}
