package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.tasks.ActionAnimationTask;
import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import it.zS0bye.eLuckyBlock.utils.ConfigUtils;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import java.util.Map;

public class ActionExecutor extends Executors {

    private final Map<Player, BukkitTask> actionTask;
    private final Map<Player, Integer> actionTicks;
    private final FileConfiguration animations;
    private final eLuckyBlock plugin;
    private final String execute;
    private final Player player;

    public ActionExecutor(final String execute, final Player player) {
        this.plugin = eLuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.animations = plugin.getAnimations().getConfig();
        this.actionTask = plugin.getActionTask();
        this.actionTicks = plugin.getActionTicks();
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation) {
        super.startTask(getAnimation);
        long interval = this.animations.getInt(getAnimation + ".interval");

        actionTicks.put(player, 0);
        actionTask.put(this.player,
                new ActionAnimationTask(this.plugin, this.player, getAnimation)
                        .runTaskTimer(this.plugin, 0L, interval));

    }

    protected String getType() {
        return "[ACTION] ";
    }

    protected void apply() {

        String msg = ColorUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%prefix%", ConfigUtils.SETTINGS_PREFIX.getString()));

        for (String getAnimation : this.animations.getKeys(false)) {
            if (msg.equals("%animation_" + getAnimation + "%")) {
                this.plugin.stopActionTask(player);
                startTask(getAnimation);
                return;
            }
        }

        this.plugin.stopActionTask(player);
        new ActionField(player, msg);

    }
}