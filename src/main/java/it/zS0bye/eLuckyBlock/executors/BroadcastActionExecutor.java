package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.tasks.ActionAnimationTask;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastActionExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private ActionAnimationTask task;

    public BroadcastActionExecutor(final String execute, final Player player) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation, Player players) {
        super.startTask(getAnimation, players);

        ActionAnimationTask task = new ActionAnimationTask(players, this.execute, getType(), getAnimation);

        task.getTicks().put(players, 0);
        task.getTask().put(players,
                task.runTaskTimerAsynchronously(this.plugin, 0L, Animations.INTERVAL.getInt(getAnimation)));
    }

    protected String getType() {
        return "[BROADCAST_ACTION] ";
    }

    protected void apply() {

        String msg = StringUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%player%", player.getName())
                .replace("%prefix%", Config.SETTINGS_PREFIX.getString()));


        Bukkit.getOnlinePlayers().forEach(players -> {
            this.task = new ActionAnimationTask(players);
            for (String getAnimation : Animations.ANIMATIONS.getKeys()) {
                if (msg.contains("%animation_" + getAnimation + "%")) {
                    this.task.stopTask();
                    startTask(getAnimation, players);
                    return;
                }
            }

            this.task.stopTask();
            new ActionField(players, msg);
        });

    }
}