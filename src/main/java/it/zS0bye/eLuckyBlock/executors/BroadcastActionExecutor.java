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