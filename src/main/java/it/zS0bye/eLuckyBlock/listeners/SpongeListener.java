package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.utils.LuckyUtils;
import it.zS0bye.eLuckyBlock.utils.CheckLucky;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SpongeAbsorbEvent;

public class SpongeListener extends LuckyUtils implements Listener  {

    private final String luckyblocks;

    public SpongeListener(final String luckyblocks) {
        super(luckyblocks);
        this.luckyblocks = luckyblocks;
    }

    @EventHandler
    public void preventWet(SpongeAbsorbEvent e) {

        Block block = e.getBlock();
        String world = block.getWorld().getName();
        Location region = block.getLocation();

        if (new CheckLucky(block, world, region, luckyblocks)
                .check()) {
            return;
        }

        if (getBoolean(getDeny_absorb())) {
            e.setCancelled(true);
        }
    }

}
