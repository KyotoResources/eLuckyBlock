package it.zS0bye.eLuckyBlock.reflections;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.checker.VersionChecker;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class BossBarField {

    private final ELuckyBlock plugin;
    private final Player player;
    private final String msg;
    private String color = "PURPLE";
    private String style = "SOLID";
    private final double progress;
    private final int seconds;

    private final Map<Player, BukkitTask> BossTimesTask;
    private final Map<Player, BossBar> BossTimes;

    public BossBarField(final ELuckyBlock plugin, final Player player, final String msg, final double progress, final int seconds) {
        this.plugin = plugin;
        this.player = player;
        this.msg = msg;
        this.progress = progress;
        this.seconds = seconds;
        this.BossTimesTask = plugin.getBossTimesTask();
        this.BossTimes = plugin.getBossTimes();
        send();
    }

    public BossBarField(final ELuckyBlock plugin, final Player player, final String msg, final String color, final double progress, final int seconds) {
        this.plugin = plugin;
        this.player = player;
        this.msg = msg;
        this.color = color;
        this.progress = progress;
        this.seconds = seconds;
        this.BossTimesTask = plugin.getBossTimesTask();
        this.BossTimes = plugin.getBossTimes();
        send();
    }

    public BossBarField(final ELuckyBlock plugin, final Player player, final String msg, final String color, final String style, final double progress, final int seconds) {
        this.plugin = plugin;
        this.player = player;
        this.msg = msg;
        this.color = color;
        this.style = style;
        this.progress = progress;
        this.seconds = seconds;
        this.BossTimesTask = plugin.getBossTimesTask();
        this.BossTimes = plugin.getBossTimes();
        send();
    }

    public void send() {

        if (VersionChecker.getV1_8()) {
            return;
        }

        if(!BossTimes.containsKey(this.player)) {
            BossBar boss = Bukkit.createBossBar(this.msg, BarColor.valueOf(this.color), BarStyle.valueOf(this.style));
            BossTimes.put(this.player, boss);
            boss.addPlayer(this.player);
            boss.setProgress(this.progress);
            boss.setVisible(true);

            if (seconds == -1) {
                return;
            }

            if (seconds >= 0) {
                if (!BossTimesTask.containsKey(this.player)) {
                    BossTimesTask.put(this.player, new BukkitRunnable() {
                        @Override
                        public void run() {
                            boss.removePlayer(player);
                            BossTimes.remove(player);
                            this.cancel();
                            BossTimesTask.remove(player);
                        }
                    }.runTaskLater(this.plugin, this.seconds * 20L));
                }
            }
        }
    }
}
