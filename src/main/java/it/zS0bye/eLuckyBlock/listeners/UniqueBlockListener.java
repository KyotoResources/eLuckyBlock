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

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.checker.LuckyChecker;
import it.zS0bye.eLuckyBlock.files.enums.Lucky;
import it.zS0bye.eLuckyBlock.mysql.SQLConversion;
import it.zS0bye.eLuckyBlock.mysql.tables.LuckyTable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UniqueBlockListener implements Listener {

    private final String luckyblocks;
    private final LuckyTable luckyTable;

    public UniqueBlockListener(final String luckyblocks) {
        this.luckyblocks = luckyblocks;
        this.luckyTable = ELuckyBlock.getInstance().getLuckyTable();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        Player player = e.getPlayer();
        ItemStack item = e.getItemInHand();
        Block block = e.getBlockPlaced();
        String world = player.getWorld().getName();
        Location region = player.getLocation();
        Location location = block.getLocation();
        String convertLoc = SQLConversion.convertLoc(location);

        if(new LuckyChecker(block, world, region, luckyblocks)
                .check())
            return;

        if(item.getItemMeta() == null)
            return;

        String displayName = item.getItemMeta().getDisplayName();
        List<String> lore = item.getItemMeta().getLore();

        if (item.getItemMeta().hasDisplayName()
        && displayName.equals(Lucky.UNIQUE_CHECK_NAME.getString(luckyblocks))) {

            if(!Lucky.UNIQUE_CHECK_LORE.contains(luckyblocks)
            || Lucky.UNIQUE_CHECK_LORE.getStringList(luckyblocks).size() == 0
            && !item.getItemMeta().hasLore()) {
                this.luckyTable.setLocation(convertLoc, luckyblocks);
                return;
            }

            if(item.getItemMeta().hasLore() &&
                    lore.equals(Lucky.UNIQUE_CHECK_LORE.getStringList(luckyblocks))) {
               this.luckyTable.setLocation(convertLoc, luckyblocks);
            }

        }
    }
}
