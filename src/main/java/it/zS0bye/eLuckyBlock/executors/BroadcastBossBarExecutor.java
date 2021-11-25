package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.reflections.BossBarField;
import it.zS0bye.eLuckyBlock.tasks.BossBarAnimationTask;
import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import java.util.Map;

public class BroadcastBossBarExecutor extends Executors {

    private final Map<Player, BukkitTask> bossBarTask;
    private final Map<Player, Integer> bossBarTicks;
    private final FileConfiguration animations;
    private final eLuckyBlock plugin;
    private final String execute;
    private final Player player;

    public BroadcastBossBarExecutor(final String execute, final Player player) {
        this.plugin = eLuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.animations = plugin.getLucky().getConfig();
        this.bossBarTask = plugin.getBossBarTask();
        this.bossBarTicks = plugin.getBossBarTicks();
        if(this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation, Player players, String color, String style, double progress, int times) {
        super.startTask(getAnimation, players, color, style, progress, times);

        long interval = this.animations.getInt(getAnimation + ".interval");

        bossBarTicks.put(players, 0);
        bossBarTask.put(players,
                new BossBarAnimationTask(this.plugin, players, getAnimation, color, style, progress, times)
                        .runTaskTimer(this.plugin, 0L, interval));

    }

    protected String getType() {
        return "[BROADCAST_BOSSBAR] ";
    }

    protected void apply() {

        String title = ColorUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%player%", player.getName())
                .split(";")[0]);

        String color = execute
                .replace(getType(), "")
                .split(";")[1];

        String style = execute
                .replace(getType(), "")
                .split(";")[2];

        double progress = Double.parseDouble(execute
                .replace(getType(), "")
                .split(";")[3]);

        int times = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[4]);

        Bukkit.getOnlinePlayers().forEach(players -> {
            for (String getAnimation : this.animations.getKeys(false)) {
                if (title.equals("%animation_" + getAnimation + "%")) {
                    this.plugin.stopBossBarTask(players);
                    this.plugin.stopBossTimesTask(players);

                    startTask(getAnimation, players,  color, style, progress, times);
                    return;
                }
            }

            this.plugin.stopBossBarTask(players);
            this.plugin.stopBossTimesTask(players);
            new BossBarField(this.plugin, players, title, color, style, progress, times);
        });
    }
}
