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
import it.zS0bye.eLuckyBlock.reflections.TitleField;
import it.zS0bye.eLuckyBlock.tasks.TitleAnimationTask;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastTitleExecutor extends Executors {

    private final String execute;
    private final Player player;
    private TitleAnimationTask task;

    public BroadcastTitleExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected void startTask(String getAnimation, Player players, String subtitle, int fadein, int stay, int fadeout) {
        super.startTask(getAnimation, players, subtitle, fadein, stay, fadeout);

        TitleAnimationTask task = new TitleAnimationTask(players, this.execute, getType(), getAnimation, subtitle, fadein, stay, fadeout);

        task.getTicks().put(players, 0);
        task.getTask().put(players,
                task.runTaskTimerAsynchronously(ELuckyBlock.getInstance(), 0L, Animations.INTERVAL.getInt(getAnimation)));



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
            this.task = new TitleAnimationTask(players);

            for (String getAnimation : Animations.ANIMATIONS.getKeys()) {
                if (title.contains("%animation_" + getAnimation + "%")) {
                    this.task.stopTask();

                    if (subtitle.equalsIgnoreCase("none")) {
                        startTask(getAnimation, players,  "", fadein, stay, fadeout);
                        return;
                    }

                    startTask(getAnimation, players, subtitle, fadein, stay, fadeout);
                    return;
                }
            }

            this.task.stopTask();

            if (subtitle.equalsIgnoreCase("none")) {
                new TitleField(players, title, "", fadein, stay, fadeout);
                return;
            }

            new TitleField(players, title, subtitle, fadein, stay, fadeout);
        });

    }
}