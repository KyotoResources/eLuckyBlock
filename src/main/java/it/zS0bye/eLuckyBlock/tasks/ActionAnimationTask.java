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

package it.zS0bye.eLuckyBlock.tasks;

import it.zS0bye.eLuckyBlock.files.enums.Animations;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class ActionAnimationTask extends BukkitRunnable {

    private final Player player;
    private String execute;
    private String type;
    private String animation;

    private final static Map<Player, BukkitTask> task = new HashMap<>();
    private final static Map<Player, Integer> ticks = new HashMap<>();

    public ActionAnimationTask(final Player player) {
        this.player = player;
    }

    public ActionAnimationTask(final Player player, final String execute, final String type, final String animation) {
        this.player = player;
        this.type = type;
        this.execute = execute;
        this.animation = animation;
    }

    @Override
    public void run() {

        String[] executor = this.execute
                .replace(this.type, "")
                .split("%animation_" + this.animation + "%");
        String[] action = Animations.TEXTS.getStringList(animation).toArray(new String[0]);

        String left = "";
        String right = "";
        if(executor.length == 1) {
            left = executor[0];
        }
        if(executor.length == 2) {
            left = executor[0];
            right = executor[1];
        }

        if (ticks.get(player) != action.length) {
            new ActionField(player,
                    StringUtils.getPapi(this.player, left + action[ticks.get(player)] + right));
        } else {
            stopTask();
        }
        ticks.put(player, ticks.get(player) + 1);
    }

    public Map<Player, BukkitTask> getTask() {
        return task;
    }

    public Map<Player, Integer> getTicks() {
        return ticks;
    }

    public void stopTask() {
        if(task.containsKey(this.player)) {
            task.get(this.player).cancel();
            task.remove(this.player);
            ticks.put(this.player, 0);
        }
    }

}
