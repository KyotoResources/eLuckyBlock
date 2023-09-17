package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.reflections.BossBarField;
import it.zS0bye.eLuckyBlock.tasks.BossBarAnimationTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BossBarExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final Player player;
    private final HooksManager hooks;
    private String execute;

    public BossBarExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.player = player;
        this.hooks = plugin.getHooks();
        if(!execute.startsWith(getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    @Override
    protected void startTask(final Player player, final String animation, final String color, final String style, final double progress, final int times) {
        super.startTask(player, animation, color, style, progress, times);

        final BossBarAnimationTask task = new BossBarAnimationTask(this.plugin, player, this.execute, getType(), animation, color, style, progress, times);

        task.getTicks().put(player, 0);
        task.getTask().put(player, task.runTaskTimerAsynchronously(this.plugin, 0L, Animations.INTERVAL.getInt(animation)));
    }

    protected String getType() {
        return "[BOSSBAR] ";
    }

    protected void apply() {

        final String title = this.hooks.getPlaceholders(this.player, execute.split(";")[0]);
        final String color = execute.split(";")[1].toUpperCase();
        final String style = execute.split(";")[2].toUpperCase();
        final double progress = Double.parseDouble(execute.split(";")[3]);
        final int times = Integer.parseInt(execute.split(";")[4]);

        if(title.startsWith("@")) {
            Bukkit.getOnlinePlayers().forEach(players -> this.run(players, title, color, style, progress, times));
            return;
        }

        this.run(this.player, title, color, style, progress, times);
    }

    private void run(final Player player, String title, final String color, final String style, final double progress, final int times) {
        title = title.replaceFirst("@", "");
        final BossBarAnimationTask task = new BossBarAnimationTask(this.plugin, player);
        for (final String animation : Animations.ANIMATIONS.getKeys()) {
            if (!title.contains("%animation_" + animation + "%")) continue;
            task.stopTask();
            task.stopTimes();

            this.startTask(player, animation, color, style, progress, times);
            return;
        }

        task.stopTask();
        task.stopTimes();
        new BossBarField(this.plugin, this.player, title, color, style, progress, times);
    }
}
