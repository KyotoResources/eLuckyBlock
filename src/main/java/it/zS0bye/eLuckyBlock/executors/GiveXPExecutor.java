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

import org.bukkit.entity.Player;

public class GiveXPExecutor extends Executors {

    private final String execute;
    private final Player player;

    public GiveXPExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected String getType() {
        return "[GIVE_XP] ";
    }

    @Override
    protected void apply() {

        int level = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[0]);

        int xp = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[1]);

        if(level != 0)
        player.giveExpLevels(level);

        if(xp != 0)
        player.giveExp(xp);
    }

}
