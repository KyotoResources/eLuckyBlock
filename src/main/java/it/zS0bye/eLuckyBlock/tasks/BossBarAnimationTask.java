package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.BossBarField;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Map;

public class BossBarAnimationTask extends BukkitRunnable {

    private final ELuckyBlock plugin;
    private final Player player;
    private final String execute;
    private final String type;
    private final String animation;
    private final String color;
    private final String style;
    private final double progress;
    private final int times;
    private final Map<Player, Integer> bossBarTicks;
    private final Map<Player, BossBar> bossTimes;

    public BossBarAnimationTask(final ELuckyBlock plugin, final Player player, final String execute, final String type, final String animation, final String color, final String style, final double progress, final int times) {
        this.plugin = plugin;
        this.player = player;
        this.execute = execute;
        this.type = type;
        this.animation = animation;
        this.color = color;
        this.style = style;
        this.progress = progress;
        this.times = times;
        this.bossBarTicks = this.plugin.getBossBarTicks();
        this.bossTimes = this.plugin.getBossTimes();
    }

    @Override
    public void run() {

        String[] executor = this.execute
                .replace(this.type, "")
                .split(";")[0]
                .split("%animation_" + this.animation + "%");
        String[] bossbar = Animations.TEXTS.getStringList(animation).toArray(new String[0]);

        String left = "";
        String right = "";
        if(executor.length == 1) {
            left = executor[0];
        }
        if(executor.length == 2) {
            left = executor[0];
            right = executor[1];
        }

        if (bossBarTicks.get(player) != bossbar.length) {
            if (bossBarTicks.get(player) == 0) {
                new BossBarField(this.plugin, player,
                        StringUtils.getPapi(this.player, bossbar[0]),
                        color, style, progress, times);
            }else {
                if(bossTimes.containsKey(player)) {
                    bossTimes.get(player).setTitle(StringUtils.getPapi(this.player, left + bossbar[bossBarTicks.get(player)] + right));
                }
            }
        } else {
            this.plugin.stopBossBarTask(player);
        }
        bossBarTicks.put(player, bossBarTicks.get(player) + 1);
    }

}