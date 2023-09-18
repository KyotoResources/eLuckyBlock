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
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class ItemExecutor extends Executors {

    private final String execute;
    private final Location location;

    public ItemExecutor(final String execute, final Location location) {
        this.execute = execute;
        this.location = location;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[ITEM] ";
    }

    protected void apply() {

        String material = execute
                .replace(getType(), "")
                .split(";")[0]
                .toUpperCase();

        int durability = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[1]);

        ItemStack item = new ItemStack(ConvertUtils.getMaterial(material), (short) durability);

        if(this.location.getWorld() == null) {
            return;
        }

        this.location.getWorld().dropItem(this.location, item);

    }
}