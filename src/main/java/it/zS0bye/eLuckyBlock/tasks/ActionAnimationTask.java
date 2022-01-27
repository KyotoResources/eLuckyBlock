package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Map;

public class ActionAnimationTask extends BukkitRunnable {

    private final ELuckyBlock plugin;
    private final Player player;
    private final String execute;
    private final String type;
    private final String animation;
    private final Map<Player, Integer> actionTicks;

    public ActionAnimationTask(final ELuckyBlock plugin, final Player player, final String execute, final String type, final String animation) {
        this.plugin = plugin;
        this.player = player;
        this.type = type;
        this.execute = execute;
        this.animation = animation;
        this.actionTicks = this.plugin.getActionTicks();
    }

    @Override
    public void run() {

        String[] executor = this.execute
                .replace(this.type, "")
                .split("%animation_" + this.animation + "%");
        String[] action = Animations.TEXTS.getStringList(animation).toArray(new String[0]);

        String left = "";
        String right = "";
        if(executor.length == 1) {
            left = executor[0];
        }
        if(executor.length == 2) {
            left = executor[0];
            right = executor[1];
        }

        if (actionTicks.get(player) != action.length) {
            new ActionField(player,
                    StringUtils.getPapi(this.player, left + action[actionTicks.get(player)] + right));
        } else {
            this.plugin.stopActionTask(player);
        }
        actionTicks.put(player, actionTicks.get(player) + 1);
    }

}
