package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.reflections.BossBarField;
import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Map;

public class BossBarAnimationTask extends BukkitRunnable {

    private final eLuckyBlock plugin;
    private final Player player;
    private final String animation;
    private final String color;
    private final String style;
    private final double progress;
    private final int times;
    private final FileConfiguration config;
    private final Map<Player, Integer> bossBarTicks;
    private final Map<Player, BossBar> bossTimes;

    public BossBarAnimationTask(final eLuckyBlock plugin, final Player player, final String animation, final String color, final String style, final double progress, final int times) {
        this.plugin = plugin;
        this.player = player;
        this.animation = animation;
        this.color = color;
        this.style = style;
        this.progress = progress;
        this.times = times;
        this.config = plugin.getAnimations().getConfig();
        this.bossBarTicks = this.plugin.getBossBarTicks();
        this.bossTimes = this.plugin.getBossTimes();
    }

    @Override
    public void run() {
        String[] bossbar = this.config.getStringList(animation + ".texts").toArray(new String[0]);
        if (bossBarTicks.get(player) != bossbar.length) {
            if (bossBarTicks.get(player) == 0) {
                new BossBarField(this.plugin, player,
                        ColorUtils.getPapi(this.player, bossbar[0]),
                        color, style, progress, times);
            }else {
                if(bossTimes.containsKey(player)) {
                    bossTimes.get(player).setTitle(ColorUtils.getPapi(this.player, bossbar[bossBarTicks.get(player)]));
                }
            }
        } else {
            this.plugin.stopBossBarTask(player);
        }
        bossBarTicks.put(player, bossBarTicks.get(player) + 1);
    }

}