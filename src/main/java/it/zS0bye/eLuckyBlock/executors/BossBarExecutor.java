package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.reflections.BossBarField;
import it.zS0bye.eLuckyBlock.tasks.BossBarAnimationTask;
import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import java.util.Map;

public class BossBarExecutor extends Executors {

    private final Map<Player, BukkitTask> bossBarTask;
    private final Map<Player, Integer> bossBarTicks;
    private final FileConfiguration animations;
    private final eLuckyBlock plugin;
    private final String execute;
    private final Player player;

    public BossBarExecutor(final String execute, final Player player) {
        this.plugin = eLuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.animations = plugin.getAnimations().getConfig();
        this.bossBarTask = plugin.getBossBarTask();
        this.bossBarTicks = plugin.getBossBarTicks();
        if(this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation, String color, String style, double progress, int times) {
        super.startTask(getAnimation, color, style, progress, times);
        long interval = this.animations.getInt(getAnimation + ".interval");

        bossBarTicks.put(player, 0);
        bossBarTask.put(this.player,
                new BossBarAnimationTask(this.plugin, this.player, getAnimation, color, style, progress, times)
                        .runTaskTimer(this.plugin, 0L, interval));
    }

    protected String getType() {
        return "[BOSSBAR] ";
    }

    protected void apply() {

        String title = ColorUtils.getPapi(this.player, execute
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

        for (String getAnimation : this.animations.getKeys(false)) {
            if (title.equals("%animation_" + getAnimation + "%")) {
                this.plugin.stopBossBarTask(player);
                this.plugin.stopBossTimesTask(player);

                startTask(getAnimation, color, style, progress, times);
                return;
            }
        }

        this.plugin.stopBossBarTask(this.player);
        this.plugin.stopBossTimesTask(this.player);
        new BossBarField(this.plugin, this.player, title, color, style, progress, times);

    }
}
