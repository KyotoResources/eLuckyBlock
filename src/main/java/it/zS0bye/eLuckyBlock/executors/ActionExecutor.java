package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.tasks.ActionAnimationTask;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import java.util.Map;

public class ActionExecutor extends Executors {

    private final Map<Player, BukkitTask> actionTask;
    private final Map<Player, Integer> actionTicks;
    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;

    public ActionExecutor(final String execute, final Player player) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.actionTask = plugin.getActionTask();
        this.actionTicks = plugin.getActionTicks();
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation) {
        super.startTask(getAnimation);

        actionTicks.put(player, 0);
        actionTask.put(this.player,
                new ActionAnimationTask(this.plugin, this.player, this.execute, getType(), getAnimation)
                        .runTaskTimerAsynchronously(this.plugin, 0L, Animations.INTERVAL.getInt(getAnimation)));

    }

    protected String getType() {
        return "[ACTION] ";
    }

    protected void apply() {

        String msg = StringUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%prefix%", Config.SETTINGS_PREFIX.getString()));

        for (String getAnimation : Animations.ANIMATIONS.getKeys()) {
            if (msg.contains("%animation_" + getAnimation + "%")) {
                this.plugin.stopActionTask(player);
                startTask(getAnimation);
                return;
            }
        }

        this.plugin.stopActionTask(player);
        new ActionField(player, msg);

    }
}