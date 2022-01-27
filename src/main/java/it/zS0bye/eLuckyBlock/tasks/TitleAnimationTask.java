package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.TitleField;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Map;

public class TitleAnimationTask extends BukkitRunnable {

    private final ELuckyBlock plugin;
    private final Player player;
    private final String execute;
    private final String type;
    private final String animation;
    private final String subtitle;
    private final int fadein;
    private final int stay;
    private final int fadeout;
    private final Map<Player, Integer> titleTicks;

    public TitleAnimationTask(final ELuckyBlock plugin, final Player player, final String execute, final String type, final String animation, final String subtitle, final int fadein, final int stay, final int fadeout) {
        this.plugin = plugin;
        this.player = player;
        this.execute = execute;
        this.type = type;
        this.animation = animation;
        this.subtitle = subtitle;
        this.fadein = fadein;
        this.stay = stay;
        this.fadeout = fadeout;
        this.titleTicks = this.plugin.getTitleTicks();
    }

    @Override
    public void run() {

        String[] executor = this.execute
                .replace(this.type, "")
                .split(";")[0]
                .split("%animation_" + this.animation + "%");
        String[] title = Animations.TEXTS.getStringList(animation ).toArray(new String[0]);

        String left = "";
        String right = "";
        if(executor.length == 1) {
            left = executor[0];
        }
        if(executor.length == 2) {
            left = executor[0];
            right = executor[1];
        }

        if (titleTicks.get(player) != title.length) {
            if (titleTicks.get(player) == 0) {
                new TitleField(player,
                        StringUtils.getPapi(this.player, left + title[0] + right),
                        subtitle, fadein, stay, 0);
            } else if (titleTicks.get(player) == title.length - 1) {
                new TitleField(player,
                        StringUtils.getPapi(this.player, left + title[titleTicks.get(player)] + right),
                        subtitle, 0, stay, fadeout);
            } else {
                new TitleField(player,
                        StringUtils.getPapi(this.player, left + title[titleTicks.get(player)] + right),
                        subtitle, 0, stay, 0);
            }
        } else {
            this.plugin.stopTitleTask(player);
        }
        titleTicks.put(player, titleTicks.get(player) + 1);
    }

}
