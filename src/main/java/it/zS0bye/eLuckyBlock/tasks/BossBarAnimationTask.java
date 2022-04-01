package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.BossBarField;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class BossBarAnimationTask extends BukkitRunnable {

    private final ELuckyBlock plugin;
    private final Player player;
    private String execute;
    private String type;
    private String animation;
    private String color;
    private String style;
    private double progress;
    private int times;

    private final static Map<Player, BukkitTask> task = new HashMap<>();
    private final static Map<Player, Integer> ticks = new HashMap<>();
    private final static Map<Player, BossBar> timesMap = new HashMap<>();
    private final static Map<Player, BukkitTask> timesTask = new HashMap<>();

    public BossBarAnimationTask(final ELuckyBlock plugin, final Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public BossBarAnimationTask(final ELuckyBlock plugin, final Player player, final String execute, final String type, final String animation, final String color, final String style, final double progress, final int times) {
        this.plugin = plugin;
        this.player = player;
        this.execute = execute;
        this.type = type;
        this.animation = animation;
        this.color = color;
        this.style = style;
        this.progress = progress;
        this.times = times;
    }

    @Override
    public void run() {

        String[] executor = this.execute
                .replace(this.type, "")
                .split(";")[0]
                .split("%animation_" + this.animation + "%");
        String[] bossbar = Animations.TEXTS.getStringList(animation).toArray(new String[0]);

        String left = "";
        String right = "";
        if(executor.length == 1) {
            left = executor[0];
        }
        if(executor.length == 2) {
            left = executor[0];
            right = executor[1];
        }

        if (ticks.get(player) != bossbar.length) {
            if (ticks.get(player) == 0) {
                new BossBarField(this.plugin, player,
                        StringUtils.getPapi(this.player, bossbar[0]),
                        color, style, progress, times);
            }else {
                if(timesMap.containsKey(player)) {
                    timesMap.get(player).setTitle(StringUtils.getPapi(this.player, left + bossbar[ticks.get(player)] + right));
                }
            }
        } else {
            stopTask();
        }
        ticks.put(player, ticks.get(player) + 1);
    }

    public Map<Player, BukkitTask> getTask() {
        return task;
    }

    public Map<Player, Integer> getTicks() {
        return ticks;
    }

    public Map<Player, BossBar> getTimesMap() {
        return timesMap;
    }

    public Map<Player, BukkitTask> getTimesTask() {
        return timesTask;
    }

    public void stopTask() {
        if(task.containsKey(this.player)) {
            task.get(this.player).cancel();
            task.remove(this.player);
            ticks.put(this.player, 0);
        }
    }

    public void stopTimes() {
        if(timesMap.containsKey(this.player)) {
            timesMap.get(this.player).removePlayer(this.player);
            timesMap.remove(this.player);
        }
        if(timesTask.containsKey(this.player)) {
            timesTask.get(this.player).cancel();
            timesTask.remove(this.player);
        }
    }

}