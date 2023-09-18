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

import java.util.List;

public abstract class Executors {

    protected void startTask(final String getAnimation) {}

    protected void startTask(final String getAnimation, final Player players) {}

    protected void startTask(final String getAnimation, final String color, final String style, final double progress, final int times) {}

    protected void startTask(final String getAnimation, final Player players, final String color, final String style, final double progress, final int times) {}

    protected void startTask(final String getAnimation, final String subtitle, final int fadein, final int stay, final int fadeout) {}

    protected void startTask(final String getAnimation, final Player players, final String subtitle, final int fadein, final int stay, final int fadeout) {}

    protected void startTask(final String type, final List<String> colors, final int delay, final int times) {}

    protected abstract String getType();

    protected abstract void apply();

}
