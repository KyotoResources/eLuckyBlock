package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.tasks.ActionAnimationTask;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private final HooksManager hooks;

    public ActionExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        this.hooks = plugin.getHooks();
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    @Override
    protected void startTask(final Player player, final String animation) {
        super.startTask(player, animation);

        final ActionAnimationTask task = new ActionAnimationTask(this.plugin, player, this.execute, this.getType(), animation);

        task.getTicks().put(player, 0);
        task.getTask().put(player, task.runTaskTimerAsynchronously(this.plugin, 0L, Animations.INTERVAL.getInt(animation)));
    }

    protected String getType() {
        return "[ACTION] ";
    }

    protected void apply() {

        final String msg = this.hooks.getPlaceholders(this.player, execute
                .replace(this.getType(), "")
                .replace("%prefix%", Config.SETTINGS_PREFIX.getString()));

        if(msg.startsWith("@")) {
            Bukkit.getOnlinePlayers().forEach(players -> this.run(players, msg));
            return;
        }

        this.run(this.player, msg);
    }

    private void run(final Player player, String msg) {
        msg = msg.replaceFirst("@", "");
        final ActionAnimationTask task = new ActionAnimationTask(this.plugin, player);
        for (final String animation : Animations.ANIMATIONS.getKeys()) {
            if (msg.contains("%animation_" + animation + "%")) {
                task.stopTask();
                this.startTask(player, animation);
                return;
            }
        }

        task.stopTask();
        new ActionField(player, msg);
    }
}