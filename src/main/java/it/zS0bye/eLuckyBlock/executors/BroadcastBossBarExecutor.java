package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.BossBarField;
import it.zS0bye.eLuckyBlock.tasks.BossBarAnimationTask;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import java.util.Map;

public class BroadcastBossBarExecutor extends Executors {

    private final Map<Player, BukkitTask> bossBarTask;
    private final Map<Player, Integer> bossBarTicks;
    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;

    public BroadcastBossBarExecutor(final String execute, final Player player) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.bossBarTask = plugin.getBossBarTask();
        this.bossBarTicks = plugin.getBossBarTicks();
        if(this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation, Player players, String color, String style, double progress, int times) {
        super.startTask(getAnimation, players, color, style, progress, times);

        bossBarTicks.put(players, 0);
        bossBarTask.put(players,
                new BossBarAnimationTask(this.plugin, players, this.execute, getType(), getAnimation, color, style, progress, times)
                        .runTaskTimerAsynchronously(this.plugin, 0L, Animations.INTERVAL.getInt(getAnimation)));

    }

    protected String getType() {
        return "[BROADCAST_BOSSBAR] ";
    }

    protected void apply() {

        String title = StringUtils.getPapi(this.player, execute
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
            for (String getAnimation : Animations.ANIMATIONS.getKeys()) {
                if (title.contains("%animation_" + getAnimation + "%")) {
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
