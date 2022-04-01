package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.BossBarField;
import it.zS0bye.eLuckyBlock.tasks.BossBarAnimationTask;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.entity.Player;

public class BossBarExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private final BossBarAnimationTask task;

    public BossBarExecutor(final String execute, final Player player) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.task = new BossBarAnimationTask(this.plugin, this.player);
        if(this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation, String color, String style, double progress, int times) {
        super.startTask(getAnimation, color, style, progress, times);

        BossBarAnimationTask task = new BossBarAnimationTask(this.plugin, this.player, this.execute, getType(), getAnimation, color, style, progress, times);

        task.getTicks().put(player, 0);
        task.getTask().put(this.player,
                task.runTaskTimerAsynchronously(this.plugin, 0L, Animations.INTERVAL.getInt(getAnimation)));
    }

    protected String getType() {
        return "[BOSSBAR] ";
    }

    protected void apply() {

        String title = StringUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .split(";")[0]);

        String color = execute
                .replace(getType(), "")
                .split(";")[1]
                .toUpperCase();

        String style = execute
                .replace(getType(), "")
                .split(";")[2]
                .toUpperCase();

        double progress = Double.parseDouble(execute
                .replace(getType(), "")
                .split(";")[3]);

        int times = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[4]);

        for (String getAnimation : Animations.ANIMATIONS.getKeys()) {
            if (title.contains("%animation_" + getAnimation + "%")) {
                this.task.stopTask();
                this.task.stopTimes();

                startTask(getAnimation, color, style, progress, times);
                return;
            }
        }

        this.task.stopTask();
        this.task.stopTimes();
        new BossBarField(this.plugin, this.player, title, color, style, progress, times);

    }
}
