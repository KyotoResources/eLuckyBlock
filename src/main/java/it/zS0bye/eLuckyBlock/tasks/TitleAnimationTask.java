package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.reflections.TitleField;
import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Map;

public class TitleAnimationTask extends BukkitRunnable {

    private final eLuckyBlock plugin;
    private final Player player;
    private final String animation;
    private final String subtitle;
    private final int fadein;
    private final int stay;
    private final int fadeout;
    private final FileConfiguration config;
    private final Map<Player, Integer> titleTicks;

    public TitleAnimationTask(final eLuckyBlock plugin, final Player player, final String animation, final String subtitle, final int fadein, final int stay, final int fadeout) {
        this.plugin = plugin;
        this.player = player;
        this.animation = animation;
        this.subtitle = subtitle;
        this.fadein = fadein;
        this.stay = stay;
        this.fadeout = fadeout;
        this.config = plugin.getAnimations().getConfig();
        this.titleTicks = this.plugin.getTitleTicks();
    }

    @Override
    public void run() {
        String[] title = this.config.getStringList(animation + ".texts").toArray(new String[0]);
        if (titleTicks.get(player) != title.length) {
            if (titleTicks.get(player) == 0) {
                new TitleField(player,
                        ColorUtils.getPapi(this.player, title[0]),
                        subtitle, fadein, stay, 0);
            } else if (titleTicks.get(player) == title.length - 1) {
                new TitleField(player,
                        ColorUtils.getPapi(this.player, title[titleTicks.get(player)]),
                        subtitle, 0, stay, fadeout);
            } else {
                new TitleField(player,
                        ColorUtils.getPapi(this.player, title[titleTicks.get(player)]),
                        subtitle, 0, stay, 0);
            }
        } else {
            this.plugin.stopTitleTask(player);
        }
        titleTicks.put(player, titleTicks.get(player) + 1);
    }

}
