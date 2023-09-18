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

import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class EffectExecutor extends Executors {

    private final String execute;
    private final Player player;

    public EffectExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[EFFECT] ";
    }

    protected void apply() {

        String effect = execute
                .replace(getType(), "")
                .split(";")[0]
                .toUpperCase();
        int level = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[1]);
        int time = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[2]);

        player.addPotionEffect(new PotionEffect(ConvertUtils.getPotion(effect), time, level));

    }
}