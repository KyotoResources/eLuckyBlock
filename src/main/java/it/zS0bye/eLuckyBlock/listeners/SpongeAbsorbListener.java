package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.LuckyBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SpongeAbsorbEvent;

public class SpongeAbsorbListener implements Listener {

    private final ELuckyBlock plugin;

    public SpongeAbsorbListener(final ELuckyBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpongeAbsorb(final SpongeAbsorbEvent event) {
        if(event.isCancelled()) return;
        this.plugin.getLuckyblocks().forEach(luckyblock -> {
            if(new LuckyBlock(this.plugin, null, luckyblock).canAbsorb()) return;
            event.setCancelled(true);
        });
    }

}
