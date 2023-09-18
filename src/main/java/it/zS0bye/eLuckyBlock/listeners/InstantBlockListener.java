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
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.hooks.enums.Hooks;
import it.zS0bye.eLuckyBlock.objects.OpenLuckyBlock;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InstantBlockListener implements Listener {

    private final String luckyblocks;

    public InstantBlockListener(final String luckyblocks) {
        this.luckyblocks = luckyblocks;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        String world = player.getWorld().getName();
        Location region = player.getLocation();

        if(block == null)
            return;

        if(!Lucky.INSTANT_BREAK.getBoolean(luckyblocks))
            return;

        if (new LuckyChecker(player, block, world, region, luckyblocks)
                .check())
            return;

        if(Hooks.PLOTSQUARED.isCheck() &&
                !HooksManager.checkPlot(player.getUniqueId()))
            return;

        if(e.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        if(player.getGameMode() == GameMode.CREATIVE)
            return;

        OpenLuckyBlock luckyblock = new OpenLuckyBlock();
        luckyblock.open(luckyblocks, player, block, e, true);
    }

}
