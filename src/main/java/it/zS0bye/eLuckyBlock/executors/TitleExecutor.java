package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.TitleField;
import it.zS0bye.eLuckyBlock.tasks.TitleAnimationTask;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.entity.Player;

public class TitleExecutor extends Executors {

    private final String execute;
    private final Player player;
    private final TitleAnimationTask task;

    public TitleExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        this.task = new TitleAnimationTask(this.player);
        if(this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation, String subtitle, int fadein, int stay, int fadeout) {
        super.startTask(getAnimation, subtitle, fadein, stay, fadeout);

        TitleAnimationTask task = new TitleAnimationTask(this.player, this.execute, getType(), getAnimation, subtitle, fadein, stay, fadeout);

        task.getTicks().put(player, 0);
        task.getTask().put(this.player,
                task.runTaskTimerAsynchronously(ELuckyBlock.getInstance(), 0L, Animations.INTERVAL.getInt(getAnimation)));
    }

    protected String getType() {
        return "[TITLE] ";
    }

    protected void apply() {

        String title = StringUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .split(";")[0]);

        String subtitle = StringUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .split(";")[1]);
        int fadein = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[2]);
        int stay = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[3]);
        int fadeout = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[4]);

        for (String getAnimation : Animations.ANIMATIONS.getKeys()) {

            if (title.contains("%animation_" + getAnimation + "%")) {
                this.task.stopTask();

                if (subtitle.equalsIgnoreCase("none")) {
                    startTask(getAnimation, "", fadein, stay, fadeout);
                    return;
                }

                startTask(getAnimation, subtitle, fadein, stay, fadeout);
                return;
            }
        }

        this.task.stopTask();

        if(subtitle.equalsIgnoreCase("none")) {
            new TitleField(this.player, title, "", fadein, stay, fadeout);
            return;
        }

        new TitleField(this.player, title, subtitle, fadein, stay, fadeout);

    }
}
