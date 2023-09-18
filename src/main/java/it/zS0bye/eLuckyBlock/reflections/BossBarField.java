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

package it.zS0bye.eLuckyBlock.reflections;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.tasks.BossBarAnimationTask;
import it.zS0bye.eLuckyBlock.checker.VersionChecker;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarField {

    private final ELuckyBlock plugin;
    private final Player player;
    private final String msg;
    private final String color;
    private final String style;
    private final double progress;
    private final int seconds;
    private final BossBarAnimationTask task;

    public BossBarField(final ELuckyBlock plugin, final Player player, final String msg, final String color, final String style, final double progress, final int seconds) {
        this.plugin = plugin;
        this.player = player;
        this.msg = msg;
        this.color = color;
        this.style = style;
        this.progress = progress;
        this.seconds = seconds;
        this.task = new BossBarAnimationTask(this.plugin, this.player);
        send();
    }

    public void send() {

        if (VersionChecker.getV1_8()) {
            return;
        }

        if(!task.getTimesMap().containsKey(this.player)) {
            BossBar boss = Bukkit.createBossBar(this.msg, BarColor.valueOf(this.color), BarStyle.valueOf(this.style));
            task.getTimesMap().put(this.player, boss);
            boss.addPlayer(this.player);
            boss.setProgress(this.progress);
            boss.setVisible(true);

            if (seconds == -1) {
                return;
            }

            if (seconds >= 0) {
                if (!this.task.getTimesTask().containsKey(this.player)) {
                    this.task.getTimesTask().put(this.player, new BukkitRunnable() {
                        @Override
                        public void run() {
                            boss.removePlayer(player);
                            task.getTimesMap().remove(player);
                            this.cancel();
                            task.getTimesTask().remove(player);
                        }
                    }.runTaskLater(this.plugin, this.seconds * 20L));
                }
            }
        }
    }
}
