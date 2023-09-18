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

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SpawnMobExecutor extends Executors {

    private final String execute;
    private final Location location;

    public SpawnMobExecutor(final String execute, final Location location) {
        this.execute = execute;
        this.location = location;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[SPAWNMOB] ";
    }

    protected void apply() {

        String mob = execute
                .replace(getType(), "")
                .toUpperCase();

        if(this.location.getWorld() == null) {
            return;
        }

        this.location.getWorld().spawnEntity(this.location, EntityType.valueOf(mob));

    }
}