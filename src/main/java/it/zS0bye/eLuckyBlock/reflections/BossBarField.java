package it.zS0bye.eLuckyBlock.reflections;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.tasks.BossBarAnimationTask;
import it.zS0bye.eLuckyBlock.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarField {

    private final ELuckyBlock plugin;
    private final Player player;
    private final String msg;
    private final String color;
    private final String style;
    private final double progress;
    private final int seconds;
    private final BossBarAnimationTask task;

    public BossBarField(final ELuckyBlock plugin, final Player player, final String msg, final String color, final String style, final double progress, final int seconds) {
        this.plugin = plugin;
        this.player = player;
        this.msg = msg;
        this.color = color;
        this.style = style;
        this.progress = progress;
        this.seconds = seconds;
        this.task = new BossBarAnimationTask(this.plugin, this.player);
        send();
    }

    public void send() {

        if (VersionUtils.checkVersion(1.8)) return;

        if (task.getTimesMap().containsKey(this.player)) return;
        final BossBar boss = Bukkit.createBossBar(this.msg, BarColor.valueOf(this.color), BarStyle.valueOf(this.style));
        task.getTimesMap().put(this.player, boss);
        boss.addPlayer(this.player);
        boss.setProgress(this.progress);
        boss.setVisible(true);

        if (seconds < 1) return;
        if (!this.task.getTimesTask().containsKey(this.player)) {
            this.task.getTimesTask().put(this.player, new BukkitRunnable() {
                @Override
                public void run() {
                    boss.removePlayer(player);
                    task.getTimesMap().remove(player);
                    this.cancel();
                    task.getTimesTask().remove(player);
                }
            }.runTaskLater(this.plugin, this.seconds * 20L));
        }
    }
}
