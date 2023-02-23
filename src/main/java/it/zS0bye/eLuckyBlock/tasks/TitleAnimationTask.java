package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.reflections.TitleField;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class TitleAnimationTask extends BukkitRunnable {

    private final Player player;
    private final HooksManager hooks;
    private String execute;
    private String type;
    private String animation;
    private String subtitle;
    private int fadein;
    private int stay;
    private int fadeout;

    private final static Map<Player, BukkitTask> task = new HashMap<>();
    private final static Map<Player, Integer> ticks = new HashMap<>();

    public TitleAnimationTask(final ELuckyBlock plugin, final Player player) {
        this.player = player;
        this.hooks = plugin.getHooks();
    }

    public TitleAnimationTask(final ELuckyBlock plugin, final Player player, final String execute, final String type, final String animation, final String subtitle, final int fadein, final int stay, final int fadeout) {
        this.player = player;
        this.execute = execute;
        this.type = type;
        this.animation = animation;
        this.subtitle = subtitle;
        this.fadein = fadein;
        this.stay = stay;
        this.fadeout = fadeout;
        this.hooks = plugin.getHooks();
    }

    @Override
    public void run() {

        final String[] executor = this.execute
                .replace(this.type, "")
                .split(";")[0]
                .split("%animation_" + this.animation + "%");
        final String[] title = Animations.TEXTS.getStringList(animation ).toArray(new String[0]);

        String left = "";
        String right = "";
        
        if(executor.length == 1) left = executor[0];
        
        if(executor.length == 2) {
            left = executor[0];
            right = executor[1];
        }

        if (ticks.get(player) != title.length) {
            if (ticks.get(player) == 0) {
                new TitleField(player,
                        this.hooks.getPlaceholders(this.player, left + title[0] + right),
                        subtitle, fadein, stay, 0);
            } else if (ticks.get(player) == title.length - 1) {
                new TitleField(player,
                        this.hooks.getPlaceholders(this.player, left + title[ticks.get(player)] + right),
                        subtitle, 0, stay, fadeout);
            } else {
                new TitleField(player,
                        this.hooks.getPlaceholders(this.player, left + title[ticks.get(player)] + right),
                        subtitle, 0, stay, 0);
            }
        } else {
            this.stopTask();
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
        if(!task.containsKey(this.player)) return;
        task.get(this.player).cancel();
        task.remove(this.player);
        ticks.put(this.player, 0);
    }

}
