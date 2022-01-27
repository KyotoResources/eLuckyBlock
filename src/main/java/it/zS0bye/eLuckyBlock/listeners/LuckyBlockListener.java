package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.checker.LuckyChecker;
import it.zS0bye.eLuckyBlock.checker.VersionChecker;
import it.zS0bye.eLuckyBlock.files.enums.Lucky;
import it.zS0bye.eLuckyBlock.methods.OpenLuckyBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LuckyBlockListener implements Listener {

    private final String luckyblocks;

    public LuckyBlockListener(final String luckyblocks) {
        this.luckyblocks = luckyblocks;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        String world = p.getWorld().getName();
        Location region = p.getLocation();

        if (new LuckyChecker(p, block, world, region, luckyblocks)
                .check()) {
            return;
        }

        if (e.isCancelled()) {
            return;
        }

        open(e, p, block);
    }

    private void open(final BlockBreakEvent e, final Player player, final Block block) {

        if (Lucky.PREVENT_DENY_PICKUP.getBoolean(luckyblocks)) {
            if (VersionChecker.getV1_8()
                    || VersionChecker.getV1_9()
                    || VersionChecker.getV1_10()
                    || VersionChecker.getV1_11()) {
                e.getBlock().setType(Material.AIR);
            } else {
                e.setDropItems(false);
            }
        }

        OpenLuckyBlock luckyblock = new OpenLuckyBlock();
        luckyblock.open(luckyblocks, player, block, e, false);
    }

}
