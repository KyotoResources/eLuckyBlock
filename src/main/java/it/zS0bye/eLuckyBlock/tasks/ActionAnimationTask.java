package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class ActionAnimationTask extends BukkitRunnable {

    private final Player player;
    private String execute;
    private String type;
    private String animation;

    private final static Map<Player, BukkitTask> task = new HashMap<>();
    private final static Map<Player, Integer> ticks = new HashMap<>();

    public ActionAnimationTask(final Player player) {
        this.player = player;
    }

    public ActionAnimationTask(final Player player, final String execute, final String type, final String animation) {
        this.player = player;
        this.type = type;
        this.execute = execute;
        this.animation = animation;
    }

    @Override
    public void run() {

        String[] executor = this.execute
                .replace(this.type, "")
                .split("%animation_" + this.animation + "%");
        String[] action = Animations.TEXTS.getStringList(animation).toArray(new String[0]);

        String left = "";
        String right = "";
        if(executor.length == 1) {
            left = executor[0];
        }
        if(executor.length == 2) {
            left = executor[0];
            right = executor[1];
        }

        if (ticks.get(player) != action.length) {
            new ActionField(player,
                    StringUtils.getPapi(this.player, left + action[ticks.get(player)] + right));
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

    public void stopTask() {
        if(task.containsKey(this.player)) {
            task.get(this.player).cancel();
            task.remove(this.player);
            ticks.put(this.player, 0);
        }
    }

}
