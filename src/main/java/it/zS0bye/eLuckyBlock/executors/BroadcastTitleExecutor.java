package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.TitleField;
import it.zS0bye.eLuckyBlock.tasks.TitleAnimationTask;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import java.util.Map;

public class BroadcastTitleExecutor extends Executors {

    private final Map<Player, BukkitTask> titleTask;
    private final Map<Player, Integer> titleTicks;
    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;

    public BroadcastTitleExecutor(final String execute, final Player player) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.titleTask = plugin.getTitleTask();
        this.titleTicks = plugin.getTitleTicks();
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation, Player players, String subtitle, int fadein, int stay, int fadeout) {
        super.startTask(getAnimation, players, subtitle, fadein, stay, fadeout);

        titleTicks.put(players, 0);
        titleTask.put(players,
                new TitleAnimationTask(this.plugin, players, this.execute, getType(), getAnimation, subtitle, fadein, stay, fadeout)
                        .runTaskTimerAsynchronously(this.plugin, 0L, Animations.INTERVAL.getInt(getAnimation)));

    }

    protected String getType() {
        return "[BROADCAST_TITLE] ";
    }

    protected void apply() {

        String title = StringUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%player%", player.getName())
                .split(";")[0]);
        String subtitle = StringUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%player%", player.getName())
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

        Bukkit.getOnlinePlayers().forEach(players -> {
            for (String getAnimation : Animations.ANIMATIONS.getKeys()) {
                if (title.contains("%animation_" + getAnimation + "%")) {
                    this.plugin.stopTitleTask(players);

                    if (subtitle.equalsIgnoreCase("none")) {
                        startTask(getAnimation, players,  "", fadein, stay, fadeout);
                        return;
                    }

                    startTask(getAnimation, players, subtitle, fadein, stay, fadeout);
                    return;
                }
            }

            this.plugin.stopTitleTask(players);

            if (subtitle.equalsIgnoreCase("none")) {
                new TitleField(players, title, "", fadein, stay, fadeout);
                return;
            }

            new TitleField(players, title, subtitle, fadein, stay, fadeout);
        });

    }
}