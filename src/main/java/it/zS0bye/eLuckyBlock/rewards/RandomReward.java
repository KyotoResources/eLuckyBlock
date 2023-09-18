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

package it.zS0bye.eLuckyBlock.rewards;

import org.jetbrains.annotations.NotNull;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomReward<E> {

    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random = new Random();
    private double total = 0;

    public void add(double weight, @NotNull E value) {
        if (weight > 0) {
            total += weight;
            map.put(total, value);
        }
    }

    @NotNull
    public E getRandomValue() {
        if (total == 0) throw new RuntimeException("Trying to get a random value from an empty RandomReward");
        final double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

}