package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.tasks.ActionAnimationTask;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import java.util.Map;

public class BroadcastActionExecutor extends Executors {

    private final Map<Player, BukkitTask> actionTask;
    private final Map<Player, Integer> actionTicks;
    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;

    public BroadcastActionExecutor(final String execute, final Player player) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.actionTask = plugin.getActionTask();
        this.actionTicks = plugin.getActionTicks();
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation, Player players) {
        super.startTask(getAnimation, players);

        actionTicks.put(players, 0);
        actionTask.put(players,
                new ActionAnimationTask(this.plugin, players, this.execute, getType(), getAnimation)
                        .runTaskTimerAsynchronously(this.plugin, 0L, Animations.INTERVAL.getInt(getAnimation)));
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
            for (String getAnimation : Animations.ANIMATIONS.getKeys()) {
                if (msg.contains("%animation_" + getAnimation + "%")) {
                    this.plugin.stopActionTask(players);
                    startTask(getAnimation, players);
                    return;
                }
            }

            this.plugin.stopActionTask(players);
            new ActionField(players, msg);
        });

    }
}