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
