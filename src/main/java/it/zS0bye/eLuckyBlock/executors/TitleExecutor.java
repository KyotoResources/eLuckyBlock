package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.reflections.TitleField;
import it.zS0bye.eLuckyBlock.tasks.TitleAnimationTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TitleExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private final HooksManager hooks;

    public TitleExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        this.hooks = plugin.getHooks();
        if(!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    @Override
    protected void startTask(final Player player, final String animation, final String subtitle, final int fadein, final int stay, final int fadeout) {
        super.startTask(player, animation, subtitle, fadein, stay, fadeout);

        final TitleAnimationTask task = new TitleAnimationTask(this.plugin, player, this.execute, this.getType(), animation, subtitle, fadein, stay, fadeout);

        task.getTicks().put(player, 0);
        task.getTask().put(player, task.runTaskTimerAsynchronously(this.plugin, 0L, Animations.INTERVAL.getInt(animation)));
    }

    protected String getType() {
        return "[TITLE] ";
    }

    protected void apply() {

        final String title = this.hooks.getPlaceholders(this.player, execute
                .replace(this.getType(), "")
                .split(";")[0]);

        final String subtitle = this.hooks.getPlaceholders(this.player, execute
                .replace(this.getType(), "")
                .split(";")[1]);
        final int fadein = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[2]);
        final int stay = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[3]);
        final int fadeout = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[4]);

        if(title.startsWith("@")) {
            Bukkit.getOnlinePlayers().forEach(players -> this.run(players, title, subtitle, fadein, stay, fadeout));
            return;
        }

        this.run(this.player, title, subtitle, fadein, stay, fadeout);
    }

    private void run(final Player player, String title, final String subtitle, final int fadein, final int stay, final int fadeout) {
        title = title.replaceFirst("@", "");

        final TitleAnimationTask task = new TitleAnimationTask(this.plugin, player);
        for (final String animation : Animations.ANIMATIONS.getKeys()) {

            if (title.contains("%animation_" + animation + "%")) {
                task.stopTask();

                if (subtitle.equalsIgnoreCase("none")) {
                    this.startTask(player, animation, "", fadein, stay, fadeout);
                    return;
                }

                this.startTask(player, animation, subtitle, fadein, stay, fadeout);
                return;
            }
        }

        task.stopTask();

        if(subtitle.equalsIgnoreCase("none")) {
            new TitleField(this.player, title, "", fadein, stay, fadeout);
            return;
        }

        new TitleField(this.player, title, subtitle, fadein, stay, fadeout);
    }
}
