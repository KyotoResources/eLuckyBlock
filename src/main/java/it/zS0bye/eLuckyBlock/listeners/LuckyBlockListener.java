package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LuckyBlockListener extends LuckyUtils implements Listener {

    private final String luckyblocks;

    public LuckyBlockListener(final String luckyblocks) {
        super(luckyblocks);
        this.luckyblocks = luckyblocks;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        String world = p.getWorld().getName();
        Location region = p.getLocation();

        if (new CheckLucky(p, block, world, region, luckyblocks)
                .check()) {
            return;
        }

        if (e.isCancelled()) {
            return;
        }

        open(e, p, block.getLocation());
    }

    private void open(final BlockBreakEvent e, final Player player, final Location location) {
        if (getBoolean(getDeny_pickup())) {

            if (VersionChecker.getV1_8()
                    || VersionChecker.getV1_9()
                    || VersionChecker.getV1_10()
                    || VersionChecker.getV1_11()) {
                e.getBlock().setType(Material.AIR);
            } else {
                e.setDropItems(false);
            }

        }

        open(luckyblocks, player, location, e);
    }

}
