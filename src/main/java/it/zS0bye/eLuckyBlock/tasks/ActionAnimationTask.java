package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Map;

public class ActionAnimationTask extends BukkitRunnable {

    private final eLuckyBlock plugin;
    private final Player player;
    private final String animation;
    private final FileConfiguration config;
    private final Map<Player, Integer> actionTicks;

    public ActionAnimationTask(final eLuckyBlock plugin, final Player player, final String animation) {
        this.plugin = plugin;
        this.player = player;
        this.animation = animation;
        this.config = plugin.getAnimations().getConfig();
        this.actionTicks = this.plugin.getActionTicks();
    }

    @Override
    public void run() {
        String[] action = this.config.getStringList(animation + ".texts").toArray(new String[0]);
        if (actionTicks.get(player) != action.length) {
            new ActionField(player,
                    ColorUtils.getPapi(this.player, action[actionTicks.get(player)]));
        } else {
            this.plugin.stopActionTask(player);
        }
        actionTicks.put(player, actionTicks.get(player) + 1);
    }

}
