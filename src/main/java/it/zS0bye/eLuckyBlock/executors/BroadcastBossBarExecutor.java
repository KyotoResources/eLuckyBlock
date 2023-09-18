/*
 * The LuckyBlock Plugin you needed! - https://github.com/KyotoResources/eLuckyBlock
 * Copyright (c) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.BossBarField;
import it.zS0bye.eLuckyBlock.tasks.BossBarAnimationTask;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import java.util.Map;

public class BroadcastBossBarExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private BossBarAnimationTask task;

    public BroadcastBossBarExecutor(final String execute, final Player player) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        if(this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation, Player players, String color, String style, double progress, int times) {
        super.startTask(getAnimation, players, color, style, progress, times);

        BossBarAnimationTask task = new BossBarAnimationTask(this.plugin, players, this.execute, getType(), getAnimation, color, style, progress, times);

        task.getTicks().put(players, 0);
        task.getTask().put(players,
                task.runTaskTimerAsynchronously(this.plugin, 0L, Animations.INTERVAL.getInt(getAnimation)));

    }

    protected String getType() {
        return "[BROADCAST_BOSSBAR] ";
    }

    protected void apply() {

        String title = StringUtils.getPapi(this.player, execute
                .replace(getType(), "")
                .replace("%player%", player.getName())
                .split(";")[0]);

        String color = execute
                .replace(getType(), "")
                .split(";")[1];

        String style = execute
                .replace(getType(), "")
                .split(";")[2];

        double progress = Double.parseDouble(execute
                .replace(getType(), "")
                .split(";")[3]);

        int times = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[4]);

        Bukkit.getOnlinePlayers().forEach(players -> {
            this.task = new BossBarAnimationTask(this.plugin, players);

            for (String getAnimation : Animations.ANIMATIONS.getKeys()) {
                if (title.contains("%animation_" + getAnimation + "%")) {
                    this.task.stopTask();
                    this.task.stopTimes();

                    startTask(getAnimation, players,  color, style, progress, times);
                    return;
                }
            }

            this.task.stopTask();
            this.task.stopTimes();
            new BossBarField(this.plugin, players, title, color, style, progress, times);
        });
    }
}
