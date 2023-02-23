package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.LuckyBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SpongeAbsorbEvent;

public class SpongeListener extends LuckyBlock implements Listener  {

    public SpongeListener(final ELuckyBlock plugin, final String luckyblocks) {
        super(plugin, luckyblocks);
    }

    @EventHandler
    public void preventWet(final SpongeAbsorbEvent e) {

        if(!isLuckyBlock(e.getBlock()) || !canAbsorb(e)) return;
        e.setCancelled(true);
    }

}
