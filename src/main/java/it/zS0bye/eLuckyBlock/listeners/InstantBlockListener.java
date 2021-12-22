package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.utils.CheckLucky;
import it.zS0bye.eLuckyBlock.utils.LuckyUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InstantBlockListener extends LuckyUtils implements Listener {

    private final String luckyblocks;

    public InstantBlockListener(final String luckyblocks) {
        super(luckyblocks);
        this.luckyblocks = luckyblocks;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        String world = player.getWorld().getName();
        Location region = player.getLocation();

        if(block == null) {
            return;
        }

        if (new CheckLucky(player, block, world, region, luckyblocks)
                .check()) {
            return;
        }

        if(!getBoolean(getInstant_break())) {
            return;
        }

        if(e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        open(luckyblocks, player, block, e);
    }

}
