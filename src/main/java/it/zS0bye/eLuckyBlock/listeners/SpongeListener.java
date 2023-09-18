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

package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.checker.LuckyChecker;
import it.zS0bye.eLuckyBlock.files.enums.Lucky;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SpongeAbsorbEvent;

public class SpongeListener implements Listener  {

    private final String luckyblocks;

    public SpongeListener(final String luckyblocks) {
        this.luckyblocks = luckyblocks;
    }

    @EventHandler
    public void preventWet(SpongeAbsorbEvent e) {

        Block block = e.getBlock();
        String world = block.getWorld().getName();
        Location region = block.getLocation();

        if (new LuckyChecker(block, world, region, luckyblocks)
                .check())
            return;

        if (Lucky.PREVENT_DENY_ABSORB.getBoolean(luckyblocks))
            e.setCancelled(true);
    }

}
