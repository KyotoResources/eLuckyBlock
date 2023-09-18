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

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.objects.LaunchFirework;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimerFireworksTask extends BukkitRunnable {

    private final ELuckyBlock plugin;
    private final Player player;
    private Location location;
    private String type;
    private List<String> colors;
    private int times;

    private final static Map<Player, Integer> ticks = new HashMap<>();
    private final static Map<Player, BukkitTask> task = new HashMap<>();

    public TimerFireworksTask(final ELuckyBlock plugin, final Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public TimerFireworksTask(final ELuckyBlock plugin, final Player player, final Location location, final String type, final List<String> colors, final int times) {
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        this.type = type;
        this.colors = colors;
        this.times = times;
    }

    @Override
    public void run() {

        if(ticks.get(player) >= times) {
            stopTask();
        }
        new LaunchFirework(plugin, location, type, colors);
        ticks.put(player, ticks.get(player)+1);

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
