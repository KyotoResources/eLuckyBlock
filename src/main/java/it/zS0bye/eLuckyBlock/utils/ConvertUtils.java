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

package it.zS0bye.eLuckyBlock.utils;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

public class ConvertUtils {

    public static Material getMaterial(final String material) {
        return Material.getMaterial(material);
    }

    public static Effect getParticles(final String particles) {
        return Effect.valueOf(particles);
    }

    public static PotionEffectType getPotion(final String potion) {
        return PotionEffectType.getByName(potion);
    }
}
